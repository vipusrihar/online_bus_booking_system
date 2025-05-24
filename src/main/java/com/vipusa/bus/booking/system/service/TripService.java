package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.CreateTripRequest;
import com.vipusa.bus.booking.system.entity.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TripService {

    Trip createTrip(CreateTripRequest request);

    Optional<Trip> getTripById(Long tripId);

    List<Trip> getAllTrip();

    List<Trip> getTripStaringAtASpecificDay(String date);

    List<Trip> getTripEndingAtASpecificDay(String date);

    List<Trip> getAllTripSpecificBus(Long busId);

    List<Trip> getAllTripSpecificRoute(Long routeId);

    List<Trip> getAllTripWithinSpecificTime(String startTime, String EndTime);

    boolean deleteTrip(Long TripId);

}
