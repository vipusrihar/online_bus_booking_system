package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.request.CreateTripRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip createTrip(CreateTripRequest request);
    Optional<Trip> findTripById(Long tripId);
    List<Trip> getAllTrips();
    List<Trip> getTripsStartingAtDate(LocalDate date);
    List<Trip> getTripsEndingAtDate(LocalDate date);
    List<Trip> getAllTripsForBus(Long busId);
    List<Trip> getAllTripsForRoute(Long routeId);
    List<Trip> getTripsBetweenDates(LocalDate start, LocalDate end);
    boolean deleteTrip(Long tripId);
    Trip updateTripStatus(Long tripId, com.vipusa.bus.booking.system.defaults.TripStatus status);
}