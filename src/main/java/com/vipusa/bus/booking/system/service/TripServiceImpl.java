package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.CreateTripRequest;
import com.vipusa.bus.booking.system.entity.Trip;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TripServiceImpl implements TripService {
    @Override
    public Trip createTrip(CreateTripRequest request) {
        return null;
    }

    @Override
    public Optional<Trip> getTripById(Long tripId) {
        return Optional.empty();
    }

    @Override
    public List<Trip> getAllTrip() {
        return null;
    }

    @Override
    public List<Trip> getTripStaringAtASpecificDay(String date) {
        return null;
    }

    @Override
    public List<Trip> getTripEndingAtASpecificDay(String date) {
        return null;
    }

    @Override
    public List<Trip> getAllTripSpecificBus(Long busId) {
        return null;
    }

    @Override
    public List<Trip> getAllTripSpecificRoute(Long routeId) {
        return null;
    }

    @Override
    public List<Trip> getAllTripWithinSpecificTime(String startTime, String EndTime) {
        return null;
    }

    @Override
    public boolean deleteTrip(Long TripId) {
        return false;
    }
}
