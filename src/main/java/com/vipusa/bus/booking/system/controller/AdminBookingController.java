package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.service.BookingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/booking")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/allBookings/{tripId}")
    public ResponseEntity<ApiResponse<?>> getAllBookingsByTrip(@PathVariable Long tripId) {
        List<?> bookings = bookingService.getAllBookingByTripId(tripId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bookings for trip retrieved successfully")
                .response(bookings)
                .build());
    }

    @GetMapping("/allBookings/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getAllBookingsByUser(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllBookingByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bookings for user retrieved successfully")
                .response(bookings)
                .build());
    }

    @PutMapping("/changeBooking/{userId}/{bookingId}")
    public ResponseEntity<ApiResponse<?>> changeBooking(
            @PathVariable Long userId,
            @PathVariable Long bookingId,
            @RequestBody @Valid EditBookingRequest request) {
        var booking = bookingService.changeBooking(userId, bookingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Booking changed successfully")
                .response(booking)
                .build());
    }

    @GetMapping("/allBookings")
    public ResponseEntity<ApiResponse<?>> getAllBookings() {
        List<?> bookings = bookingService.getAllBooking();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All bookings retrieved successfully")
                .response(bookings)
                .build());
    }
}