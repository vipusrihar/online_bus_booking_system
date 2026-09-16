package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.defaults.SeatStatus;
import com.vipusa.bus.booking.system.defaults.TripStatus;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.entity.Seat;
import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.exception.BusNotFoundException;
import com.vipusa.bus.booking.system.exception.RouteNotFoundException;
import com.vipusa.bus.booking.system.exception.TripNotFoundException;
import com.vipusa.bus.booking.system.repository.BusRepository;
import com.vipusa.bus.booking.system.repository.RouteRepository;
import com.vipusa.bus.booking.system.repository.SeatRepository;
import com.vipusa.bus.booking.system.repository.TripRepository;
import com.vipusa.bus.booking.system.request.CreateTripRequest;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final SeatRepository seatRepository;

    public TripServiceImpl(TripRepository tripRepository,
                           BusRepository busRepository,
                           RouteRepository routeRepository,
                           SeatRepository seatRepository) {
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional
    public Trip createTrip(CreateTripRequest request) {
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new BusNotFoundException("No Bus Found with ID " + request.getBusId()));

        // Check for overlapping trips on the same bus
        List<Trip> existingTrips = tripRepository.findByBus_Id(request.getBusId());
        LocalDateTime newStart = request.getDepartsAt();
        LocalDateTime newEnd = request.getArrivesAt();

        if (newStart.isAfter(newEnd) || newStart.isEqual(newEnd)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }

        for (Trip existing : existingTrips) {
            // Check for overlap
            if (newStart.isBefore(existing.getArrivesAt()) && newEnd.isAfter(existing.getDepartsAt())) {
                throw new IllegalArgumentException("Trip time overlaps with an existing trip for this bus");
            }

            // Check if there's at least 9 hours break
            if (newStart.isAfter(existing.getArrivesAt())) {
                Duration breakDuration = Duration.between(existing.getArrivesAt(), newStart);
                if (breakDuration.toHours() < 9) {
                    throw new IllegalArgumentException("Bus must have at least 9 hours break between trips");
                }
            }

            if (newEnd.isBefore(existing.getDepartsAt())) {
                Duration breakDuration = Duration.between(newEnd, existing.getDepartsAt());
                if (breakDuration.toHours() < 9) {
                    throw new IllegalArgumentException("Bus must have at least 9 hours break between trips");
                }
            }
        }

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RouteNotFoundException("No Route Found with ID " + request.getRouteId()));

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartsAt(newStart);
        trip.setArrivesAt(newEnd);
        trip.setPrice(request.getPrice());
        trip.setStatus(TripStatus.SCHEDULED);

        Trip savedTrip = tripRepository.save(trip);

        // Initialize seats for this trip
        int totalSeats = bus.getTotalSeats();
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setTrip(savedTrip);
            seat.setSeatNumber(i);
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }

        log.info("Created trip {} for bus {} on route {} with {} seats", 
                savedTrip.getId(), bus.getId(), route.getId(), totalSeats);
        return savedTrip;
    }

    @Override
    public Optional<Trip> findTripById(Long tripId) {
        return tripRepository.findById(tripId);
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public List<Trip> getTripsStartingAtDate(LocalDate date) {
        return tripRepository.findByStartDate(date);
    }

    @Override
    public List<Trip> getTripsEndingAtDate(LocalDate date) {
        return tripRepository.findByEndDate(date);
    }

    @Override
    public List<Trip> getAllTripsForBus(Long busId) {
        return tripRepository.findByBus_Id(busId);
    }

    @Override
    public List<Trip> getAllTripsForRoute(Long routeId) {
        return tripRepository.findByRoute_Id(routeId);
    }

    @Override
    public List<Trip> getTripsBetweenDates(LocalDate start, LocalDate end) {
        return tripRepository.findByStartDateBetween(start, end);
    }

    @Override
    @Transactional
    public boolean deleteTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new TripNotFoundException("Trip not found with id: " + tripId);
        }
        tripRepository.deleteById(tripId);
        return true;
    }

    @Override
    @Transactional
    public Trip updateTripStatus(Long tripId, TripStatus status) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + tripId));
        trip.setStatus(status);
        return tripRepository.save(trip);
    }
}