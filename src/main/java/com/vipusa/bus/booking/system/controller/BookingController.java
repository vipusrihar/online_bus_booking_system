package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
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
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{userId}/create")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createBooking(
            @PathVariable Long userId,
            @RequestBody @Valid CreateBookingRequest bookingRequest) {
        var booking = bookingService.createBooking(userId, bookingRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Booking created successfully")
                .response(booking)
                .build());
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("@bookingService.getBookingById(#bookingId).user.id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getBookingById(@PathVariable Long bookingId) {
        var booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Booking retrieved successfully")
                .response(booking)
                .build());
    }

    @PutMapping("/{userId}/cancel/{bookingId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> cancelBooking(
            @PathVariable Long userId,
            @PathVariable Long bookingId) {
        boolean result = bookingService.cancelBooking(userId, bookingId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Booking cancelled successfully")
                .response(result)
                .build());
    }

    @GetMapping("/{userId}/canceledBookings")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getCanceledBookings(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllCanceledBookingsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Canceled bookings retrieved successfully")
                .response(bookings)
                .build());
    }

    @GetMapping("/{userId}/allBookings/active")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getActiveBookings(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllActiveBookingByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Active bookings retrieved successfully")
                .response(bookings)
                .build());
    }

    @GetMapping("/{userId}/allBookings/hold")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getHoldBookings(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllHoldBookingByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Held bookings retrieved successfully")
                .response(bookings)
                .build());
    }

    @GetMapping("/{userId}/allBookings/changed")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getChangedBookings(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllChangedBookingByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Changed bookings retrieved successfully")
                .response(bookings)
                .build());
    }

    @GetMapping("/{userId}/allBookings")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllUserBookings(@PathVariable Long userId) {
        List<?> bookings = bookingService.getAllBookingByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All bookings retrieved successfully")
                .response(bookings)
                .build());
    }
}