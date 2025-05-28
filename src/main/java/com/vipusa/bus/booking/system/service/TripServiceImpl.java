package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.repository.BusRepository;
import com.vipusa.bus.booking.system.repository.RouteRepository;
import com.vipusa.bus.booking.system.repository.TripRepository;
import com.vipusa.bus.booking.system.request.CreateTripRequest;
import com.vipusa.bus.booking.system.entity.Trip;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TripServiceImpl implements TripService {

    private TripRepository tripRepository;

    private BusRepository busRepository;

    private RouteRepository routeRepository;

    public TripServiceImpl(TripRepository tripRepository,
                           BusRepository busRepository,
                           RouteRepository routeRepository){
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
    }
    @Override
    public Trip createTrip(CreateTripRequest request) {

        Trip trip = new Trip();
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("No Bus Found with ID "+ request.getBusId()));
        trip.setBus(bus);

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("No Route Found with ID "+ request.getRouteId()));
        trip.setRoute(route);

        trip.setStartDate(request.getStartDate());
        trip.setStartTime(request.getStartTime());
        trip.setEndDate(request.getEndDate());
        trip.setEndTime(request.getEndTime());

        int totalSeats = bus.getTotalSeats();

        Map<Integer, Boolean> seatStatus = new HashMap<>();
        for (int i = 1; i <= totalSeats; i++) {
            seatStatus.put(i, false);
        }
        trip.setSeatStatus(seatStatus);

        return tripRepository.save(trip);
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
    public List<Trip> getTripStartingAtASpecificDay(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return tripRepository.findByStartDate(localDate);
    }

    @Override
    public List<Trip> getTripEndingAtASpecificDay(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return tripRepository.findByEndDate(localDate);
    }

    @Override
    public List<Trip> getAllTripSpecificBus(Long busId) {
        return tripRepository.findByBus_Id(busId);
    }

    @Override
    public List<Trip> getAllTripSpecificRoute(Long routeId) {
        return tripRepository.findByRoute_Id(routeId);
    }

    @Override
    public List<Trip> getAllTripStartDateBetween(LocalDate start, LocalDate end) {
        return tripRepository.findByStartDateBetween(start, end);
    }

    @Override
    public boolean deleteTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new EntityNotFoundException("Trip not found with id: " + tripId);
        }
        tripRepository.deleteById(tripId);
        return true;
    }
}
