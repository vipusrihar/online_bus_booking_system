package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.defaults.TripStatus;
import com.vipusa.bus.booking.system.request.CreateTripRequest;
import com.vipusa.bus.booking.system.service.TripService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createTrip(@RequestBody @Valid CreateTripRequest request) {
        Trip trip = tripService.createTrip(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trip created successfully")
                .response(trip)
                .build());
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<?>> getTripById(@PathVariable Long tripId) {
        Trip trip = tripService.findTripById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with ID: " + tripId));
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trip retrieved successfully")
                .response(trip)
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<?>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All trips retrieved successfully")
                .response(trips)
                .build());
    }

    @DeleteMapping("/{tripId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteTrip(@PathVariable Long tripId) {
        boolean result = tripService.deleteTrip(tripId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trip deleted successfully")
                .response(result)
                .build());
    }

    @GetMapping("/start/{date}")
    public ResponseEntity<ApiResponse<?>> getTripsStartingAtDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Trip> trips = tripService.getTripsStartingAtDate(localDate);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trips retrieved successfully")
                .response(trips)
                .build());
    }

    @GetMapping("/end/{date}")
    public ResponseEntity<ApiResponse<?>> getTripsEndingAtDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Trip> trips = tripService.getTripsEndingAtDate(localDate);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trips retrieved successfully")
                .response(trips)
                .build());
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<ApiResponse<?>> getTripsByBus(@PathVariable Long busId) {
        List<Trip> trips = tripService.getAllTripsForBus(busId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trips retrieved successfully")
                .response(trips)
                .build());
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<ApiResponse<?>> getTripsByRoute(@PathVariable Long routeId) {
        List<Trip> trips = tripService.getAllTripsForRoute(routeId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trips retrieved successfully")
                .response(trips)
                .build());
    }

    @GetMapping("/time")
    public ResponseEntity<ApiResponse<?>> getTripsBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Trip> trips = tripService.getTripsBetweenDates(start, end);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trips retrieved successfully")
                .response(trips)
                .build());
    }

    @PutMapping("/{tripId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateTripStatus(
            @PathVariable Long tripId,
            @RequestParam TripStatus status) {
        Trip trip = tripService.updateTripStatus(tripId, status);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Trip status updated successfully")
                .response(trip)
                .build());
    }
}