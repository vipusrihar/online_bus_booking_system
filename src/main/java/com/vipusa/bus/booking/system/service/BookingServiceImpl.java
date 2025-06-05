package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import com.vipusa.bus.booking.system.entity.BookingChangeLog;
import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.repository.BookingChangeLogRepository;
import com.vipusa.bus.booking.system.repository.TripRepository;
import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.repository.BookingRepository;
import com.vipusa.bus.booking.system.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BookingServiceImpl implements BookingService{

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final TripRepository tripRepository;
    
    private final BookingChangeLogRepository bookingChangeLogRepository;

    public BookingServiceImpl(
            TripRepository tripRepository, 
            BookingRepository bookingRepository, 
            UserRepository userRepository,
            BookingChangeLogRepository bookingChangeLogRepository){
        this.tripRepository=tripRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingChangeLogRepository = bookingChangeLogRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(Long userId, CreateBookingRequest bookingRequest) {
        Booking booking = new Booking();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        booking.setUser(user);
        Long tripId = bookingRequest.getTripId();
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));

        //get seat numbers from the request
        List<Integer> seatNumbers = bookingRequest.getSeatNumbers();

        Map<Integer, Boolean> seatStatus = trip.getSeatStatus();

        //check if seats are already booked
        for (Integer seat : seatNumbers) {
            if (seatStatus.containsKey(seat) && Boolean.TRUE.equals(seatStatus.get(seat))) {
                throw new RuntimeException("Seat " + seat + " is already booked.");
            }
        }

        for (Integer seat : seatNumbers) {
            seatStatus.put(seat, true);
        }

        trip.setSeatStatus(seatStatus);

        booking.setTrip(trip);
        booking.setSeatNumbers(seatNumbers);
        booking.setBookingDate(LocalDate.now());
        booking.setBookingTime(LocalTime.now());
        booking.setStatus(BookingStatus.ACTIVE);
        return bookingRepository.save(booking);
    }


    @Override
    @Transactional
    public boolean cancelBooking(Long userId, Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findByIdAndUser_Id(bookingId, userId);

        if (bookingOptional.isPresent()) {

            Booking booking = bookingOptional.get();
            LocalDateTime bookingTime = LocalDateTime.of(booking.getBookingDate(),booking.getBookingTime());
            LocalDateTime nowTime = LocalDateTime.of(LocalDate.now(),LocalTime.now());
            Duration cancelDuration1 = Duration.between(bookingTime, nowTime);

            if (cancelDuration1.toHours() < 2) {
                throw new RuntimeException("You can only Cancel within 2 hours after creating");
            }

            Trip trip = booking.getTrip();

            LocalDateTime tripStartingTime = LocalDateTime.of(trip.getStartDate(),trip.getStartTime());
            Duration cancelDuration2 = Duration.between(tripStartingTime,nowTime);
            if(cancelDuration2.toHours() < 3){
                throw new RuntimeException("You cannot  Cancel before 3 hours trip staring");
            }

            //mark booking as canceled instead of deleting
            booking.setStatus(BookingStatus.CANCELED);

            //retrieve booked seat numbers
            List<Integer> seatNumbers = booking.getSeatNumbers();
            Map<Integer, Boolean> seatStatus = trip.getSeatStatus();

            //mark seats as available (false)
            for (Integer seat : seatNumbers) {
                seatStatus.put(seat, false);
            }

            trip.setSeatStatus(seatStatus);

            bookingRepository.save(booking);  //save updated status

            return true;
        }

        return false;
    }



    @Override
    public Booking getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new RuntimeException("Booking not found with ID "+bookingId));
        return booking;
    }

    @Override
    public List<Booking> getAllActiveBookingByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.ACTIVE);
        return bookingList;
    }
    @Override
    public List<Booking> getAllBookingByTripId(Long tripId) {
        List<Booking> bookingList = bookingRepository.findByTrip_Id(tripId);
        return bookingList;
    }

    @Override
    @Transactional
    public Booking changeBooking(Long userId, Long bookId, EditBookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Booking bookingOld = bookingRepository.findById(bookId).orElseThrow(
                () -> new RuntimeException("Booking not found with id " + bookId));

        if (bookingOld.getStatus() != BookingStatus.ACTIVE) {
            throw new RuntimeException("Only active bookings can be changed.");
        }

        Trip trip = bookingOld.getTrip();
        LocalDateTime nowTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime tripStartingTime = LocalDateTime.of(trip.getStartDate(), trip.getStartTime());
        Duration changeDuration = Duration.between(nowTime, tripStartingTime);

        if (changeDuration.toHours() < 3) {
            throw new RuntimeException("Cannot change booking before 3 hours trip starting");
        }

        bookingOld.setStatus(BookingStatus.CHANGED);

        //free old seats
        List<Integer> seatNumbers = bookingOld.getSeatNumbers();
        Map<Integer, Boolean> seatStatus = trip.getSeatStatus();
        for (Integer seat : seatNumbers) {
            seatStatus.put(seat, false);
        }
        trip.setSeatStatus(seatStatus);
        bookingRepository.save(bookingOld);

        //create new booking
        Trip newTrip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + request.getTripId()));
        List<Integer> newSeatNumbers = request.getSeatNumbers();
        Map<Integer, Boolean> newSeatStatus = newTrip.getSeatStatus();

        for (Integer seat : newSeatNumbers) {
            if (newSeatStatus.containsKey(seat) && Boolean.TRUE.equals(newSeatStatus.get(seat))) {
                throw new RuntimeException("Seat " + seat + " is already booked.");
            }
        }
        for (Integer seat : newSeatNumbers) {
            newSeatStatus.put(seat, true);
        }
        newTrip.setSeatStatus(newSeatStatus);
        tripRepository.save(newTrip);

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setTrip(newTrip);
        newBooking.setSeatNumbers(newSeatNumbers);
        newBooking.setBookingDate(LocalDate.now());
        newBooking.setBookingTime(LocalTime.now());
        newBooking.setStatus(BookingStatus.ACTIVE);

        Booking savedNewBooking = bookingRepository.save(newBooking);

        //save log after new booking is saved
        BookingChangeLog log = new BookingChangeLog();
        log.setOldBooking(bookingOld);
        log.setNewBooking(savedNewBooking);
        log.setChangedAt(LocalDateTime.now());
        bookingChangeLogRepository.save(log);

        return savedNewBooking;
    }


    @Override
    public List<Booking> getAllBooking() {
        List<Booking> bookingList = bookingRepository.findAll();
        return bookingList;
    }

    @Override
    public List<Booking> getAllCanceledBookingsByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.CANCELED);
        return bookingList;
    }

    @Override
    public List<Booking> getAllHoldBookingByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.HOLED);
        return bookingList;
    }

    @Override
    public List<Booking> getAllChangedBookingByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findByUser_IdAndStatus(userId, BookingStatus.CHANGED);
        return bookingList;
    }

    @Override
    public List<Booking> getAllBookingByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findByUser_Id(userId);
        return bookingList;
    }
}
