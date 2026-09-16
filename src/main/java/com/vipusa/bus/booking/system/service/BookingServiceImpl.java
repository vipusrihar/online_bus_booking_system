package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import com.vipusa.bus.booking.system.defaults.SeatStatus;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.entity.BookingChangeLog;
import com.vipusa.bus.booking.system.entity.BookingSeat;
import com.vipusa.bus.booking.system.entity.Seat;
import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.exception.BookingNotFoundException;
import com.vipusa.bus.booking.system.exception.SeatNotAvailableException;
import com.vipusa.bus.booking.system.exception.TripNotFoundException;
import com.vipusa.bus.booking.system.exception.UserNotFoundException;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.repository.BookingChangeLogRepository;
import com.vipusa.bus.booking.system.repository.BookingRepository;
import com.vipusa.bus.booking.system.repository.BookingSeatRepository;
import com.vipusa.bus.booking.system.repository.SeatRepository;
import com.vipusa.bus.booking.system.repository.TripRepository;
import com.vipusa.bus.booking.system.repository.UserRepository;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingChangeLogRepository bookingChangeLogRepository;

    public BookingServiceImpl(
            TripRepository tripRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository,
            BookingChangeLogRepository bookingChangeLogRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.bookingChangeLogRepository = bookingChangeLogRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(Long userId, CreateBookingRequest bookingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Lock the seats pessimistically to prevent race conditions
        List<Seat> seats = lockAndValidateSeats(bookingRequest.getTripId(), bookingRequest.getSeatNumbers());

        Trip trip = tripRepository.findById(bookingRequest.getTripId())
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + bookingRequest.getTripId()));

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setTotalAmount(trip.getPrice().multiply(BigDecimal.valueOf(seats.size())));
        booking.setCreatedAt(LocalDateTime.now());

        // Create booking seats
        List<BookingSeat> bookingSeats = new ArrayList<>();
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seatRepository.save(seat);

            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);
            bookingSeats.add(bookingSeat);
        }
        booking.setBookingSeats(bookingSeats);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Created booking {} for user {} on trip {} with {} seats", 
                savedBooking.getBookingRef(), userId, trip.getId(), seats.size());
        return savedBooking;
    }

    @Override
    @Transactional
    public boolean cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findByIdAndUser_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found or access denied"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        Trip trip = booking.getTrip();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tripStart = trip.getDepartsAt();

        // Check 2-hour post-booking window
        Duration postBookingDuration = Duration.between(booking.getCreatedAt(), now);
        if (postBookingDuration.toHours() < 2) {
            throw new IllegalStateException("You can only cancel within 2 hours after booking");
        }

        // Check 3-hour pre-departure window
        Duration preDepartureDuration = Duration.between(now, tripStart);
        if (preDepartureDuration.toHours() < 3) {
            throw new IllegalStateException("Cannot cancel less than 3 hours before departure");
        }

        // Release seats
        for (BookingSeat bs : booking.getBookingSeats()) {
            Seat seat = bs.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
            bookingSeatRepository.delete(bs);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Cancelled booking {} for user {}", bookingId, userId);
        return true;
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID " + bookingId));
    }

    @Override
    public List<Booking> getAllActiveBookingByUserId(Long userId) {
        return bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.ACTIVE);
    }

    @Override
    public List<Booking> getAllBookingByTripId(Long tripId) {
        return bookingRepository.findByTrip_Id(tripId);
    }

    @Override
    @Transactional
    public Booking changeBooking(Long userId, Long bookingId, EditBookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Booking bookingOld = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (bookingOld.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalStateException("Only active bookings can be changed.");
        }

        Trip trip = bookingOld.getTrip();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tripStart = trip.getDepartsAt();
        Duration changeDuration = Duration.between(now, tripStart);

        if (changeDuration.toHours() < 3) {
            throw new IllegalStateException("Cannot change booking less than 3 hours before departure");
        }

        // Release old seats
        for (BookingSeat bs : bookingOld.getBookingSeats()) {
            Seat seat = bs.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
            bookingSeatRepository.delete(bs);
        }

        bookingOld.setStatus(BookingStatus.CHANGED);
        bookingRepository.save(bookingOld);

        // Create new booking
        Trip newTrip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + request.getTripId()));

        List<Seat> newSeats = lockAndValidateSeats(request.getTripId(), request.getSeatNumbers());

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setTrip(newTrip);
        newBooking.setStatus(BookingStatus.ACTIVE);
        newBooking.setTotalAmount(newTrip.getPrice().multiply(BigDecimal.valueOf(newSeats.size())));
        newBooking.setCreatedAt(LocalDateTime.now());

        List<BookingSeat> newBookingSeats = new ArrayList<>();
        for (Seat seat : newSeats) {
            seat.setStatus(SeatStatus.BOOKED);
            seatRepository.save(seat);

            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(newBooking);
            bookingSeat.setSeat(seat);
            newBookingSeats.add(bookingSeat);
        }
        newBooking.setBookingSeats(newBookingSeats);

        Booking savedNewBooking = bookingRepository.save(newBooking);

        // Save change log
        BookingChangeLog changeLog = new BookingChangeLog();
        changeLog.setOldBooking(bookingOld);
        changeLog.setNewBooking(savedNewBooking);
        changeLog.setChangedAt(LocalDateTime.now());
        bookingChangeLogRepository.save(changeLog);

        log.info("Changed booking {} to new booking {} for user {}", new Object[]{bookingId, savedNewBooking.getId(), userId});
        return savedNewBooking;
    }

    @Override
    public List<Booking> getAllBooking() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getAllCanceledBookingsByUserId(Long userId) {
        return bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.CANCELLED);
    }

    @Override
    public List<Booking> getAllHoldBookingByUserId(Long userId) {
        return bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.HELD);
    }

    @Override
    public List<Booking> getAllChangedBookingByUserId(Long userId) {
        return bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.CHANGED);
    }

    @Override
    public List<Booking> getAllBookingByUserId(Long userId) {
        return bookingRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional
    public Booking holdBooking(Long userId, Long bookingId, int minutes) {
        Booking booking = bookingRepository.findByIdAndUser_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found or access denied"));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalStateException("Only active bookings can be held");
        }

        booking.setStatus(BookingStatus.HELD);
        // TODO: Schedule automatic release after minutes
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking releaseHold(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (booking.getStatus() != BookingStatus.HELD) {
            throw new IllegalStateException("Only held bookings can be released");
        }

        booking.setStatus(BookingStatus.ACTIVE);
        return bookingRepository.save(booking);
    }

    private List<Seat> lockAndValidateSeats(Long tripId, List<Integer> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new IllegalArgumentException("At least one seat is required");
        }

        // Check for duplicates
        if (seatNumbers.size() != seatNumbers.stream().distinct().count()) {
            throw new IllegalArgumentException("Duplicate seat numbers are not allowed");
        }

        // Lock seats with pessimistic write lock
        List<Seat> seats = new ArrayList<>();
        for (Integer seatNumber : seatNumbers) {
            Seat seat = seatRepository.findByTripAndSeatNumberWithLock(tripId, seatNumber)
                    .orElseThrow(() -> new SeatNotAvailableException("Seat " + seatNumber + " does not exist for this trip"));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new SeatNotAvailableException("Seat " + seatNumber + " is not available (status: " + seat.getStatus() + ")");
            }
            seats.add(seat);
        }
        return seats;
    }
}