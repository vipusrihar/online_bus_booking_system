package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.CreateTripRequest;
import com.vipusa.bus.booking.system.entity.Trip;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface TripService {

    Trip createTrip(CreateTripRequest request);

    Optional<Trip> findTripById(Long tripId);

    List<Trip> getAllTrips();

    List<Trip> getTripStartingAtASpecificDay(String date);

    List<Trip> getTripEndingAtASpecificDay(String date);

    List<Trip> getAllTripSpecificBus(Long busId);

    List<Trip> getAllTripSpecificRoute(Long routeId);

    List<Trip> getAllTripStartDateBetween(LocalDate start, LocalDate end);

    boolean deleteTrip(Long TripId);

}
