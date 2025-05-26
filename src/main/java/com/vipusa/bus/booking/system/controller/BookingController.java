package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid CreateBookingRequest bookingRequest) {

        log.info("Creating booking for user ID: {}", userId);
        try {
            Booking booking = bookingService.createBooking(userId, bookingRequest);
            log.info("Booking created successfully with ID: {}", booking.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Booking>builder()
                            .response(booking)
                            .message("Booking successfully created")
                            .isSuccess(true)
                            .build());
        } catch (Exception e) {
            log.error("Failed to create booking for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Booking>builder()
                            .response(null)
                            .message("Failed to create booking: " + e.getMessage())
                            .isSuccess(false)
                            .build());
        }
    }

    @PutMapping("/{userId}/{tripId}/cancel")
    public ResponseEntity<ApiResponse<Boolean>> cancelBooking(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long tripId) {

        log.info("Attempting to cancel booking for user {} and trip {}", userId, tripId);
        try {
            boolean isCancelled = bookingService.cancelBooking(userId, tripId);

            if (!isCancelled) {
                log.warn("Cancellation failed for user {} and trip {}", userId, tripId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.<Boolean>builder()
                                .response(false)
                                .message("Failed to cancel booking - booking might not exist or cancellation period expired")
                                .isSuccess(false)
                                .build());
            }

            log.info("Booking cancelled successfully for user {} and trip {}", userId, tripId);
            return ResponseEntity.ok()
                    .body(ApiResponse.<Boolean>builder()
                            .response(true)
                            .message("Booking successfully cancelled")
                            .isSuccess(true)
                            .build());
        } catch (Exception e) {
            log.error("Error cancelling booking for user {} and trip {}: {}", userId, tripId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .response(false)
                            .message("Internal server error while cancelling booking")
                            .isSuccess(false)
                            .build());
        }
    }

    @GetMapping("/{userId}/allBookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookingOfUser(
            @PathVariable @Min(1) Long userId) {

        log.info("Fetching all bookings for user ID: {}", userId);
        try {
            List<Booking> bookings = bookingService.getAllBookingByUserId(userId);

            if (bookings.isEmpty()) {
                log.info("No bookings found for user ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<List<Booking>>builder()
                                .isSuccess(false)
                                .message("No bookings found for user ID: " + userId)
                                .response(null)
                                .build());
            }

            log.debug("Found {} bookings for user ID: {}", bookings.size(), userId);
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(true)
                            .message("Successfully retrieved bookings")
                            .response(bookings)
                            .build());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(false)
                            .message(e.getMessage())
                            .response(null)
                            .build());
        } catch (Exception e) {
            log.error("Error fetching bookings for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(false)
                            .message("Failed to retrieve bookings")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{userId}/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long bookingId) {

        log.info("Fetching booking ID {} for user ID {}", bookingId, userId);
        try {
            Booking booking = bookingService.getBookingById(bookingId, userId);

            if (booking == null) {
                log.warn("Booking not found - ID: {}, User ID: {}", bookingId, userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Booking>builder()
                                .response(null)
                                .isSuccess(false)
                                .message("Booking not found")
                                .build());
            }

            return ResponseEntity.ok()
                    .body(ApiResponse.<Booking>builder()
                            .response(booking)
                            .isSuccess(true)
                            .message("Booking retrieved successfully")
                            .build());
        } catch (Exception e) {
            log.error("Error fetching booking ID {} for user {}: {}", bookingId, userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Booking>builder()
                            .response(null)
                            .isSuccess(false)
                            .message("Failed to retrieve booking")
                            .build());
        }
    }
}