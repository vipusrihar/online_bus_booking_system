package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/booking")
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/allBookings/{tripId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookingsByTripId(
            @PathVariable @Min(1) Long tripId) {
        log.info("Fetching all bookings for trip ID: {}", tripId);
        try {
            List<Booking> bookings = bookingService.getAllBookingByTripId(tripId);

            if (bookings.isEmpty()) {
                log.info("No bookings found for trip ID: {}", tripId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<List<Booking>>builder()
                                .isSuccess(false)
                                .message("No bookings found for this trip")
                                .response(bookings)
                                .build());
            }

            log.info("Found {} bookings for trip ID: {}", bookings.size(), tripId);
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(true)
                            .message(bookings.size() + " booking(s) found")
                            .response(bookings)
                            .build());
        } catch (Exception e) {
            log.error("Error fetching bookings for trip ID {}: {}", tripId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(false)
                            .message("Failed to retrieve bookings: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/allBookings/user/{userId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookingByUserId(
            @PathVariable @Min(1) Long userId) {
        log.info("Fetching all bookings for user ID: {}", userId);
        try {
            List<Booking> bookings = bookingService.getAllBookingByUserId(userId);

            if (bookings.isEmpty()) {
                log.info("No bookings found for user ID: {}", userId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<List<Booking>>builder()
                                .isSuccess(false)
                                .message("No bookings found for this user")
                                .response(bookings)
                                .build());
            }

            log.info("Found {} bookings for user ID: {}", bookings.size(), userId);
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(true)
                            .message(bookings.size() + " booking(s) found")
                            .response(bookings)
                            .build());
        } catch (Exception e) {
            log.error("Error fetching bookings for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(false)
                            .message("Failed to retrieve bookings: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @PutMapping("/changeBooking/{userId}/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> changeBookingByUserId(
            @PathVariable @Min(1) Long bookingId,
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid EditBookingRequest changeRequest) {

        log.info("Attempting to change booking for user ID: {}", userId);
        try {
            Booking updatedBooking = bookingService.changeBooking(userId,bookingId, changeRequest);
            log.info("Successfully updated booking for user ID: {}", userId);

            return ResponseEntity.ok()
                    .body(ApiResponse.<Booking>builder()
                            .isSuccess(true)
                            .message("Booking updated successfully")
                            .response(updatedBooking)
                            .build());
        } catch (IllegalArgumentException e) {
            log.warn("Bad request when changing booking for user {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Booking>builder()
                            .isSuccess(false)
                            .message(e.getMessage())
                            .response(null)
                            .build());
        } catch (Exception e) {
            log.error("Error updating booking for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Booking>builder()
                            .isSuccess(false)
                            .message("Failed to update booking: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/allBookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        log.info("Fetching all bookings");
        try {
            List<Booking> bookings = bookingService.getAllBooking();

            if (bookings.isEmpty()) {
                log.info("No bookings found");
                return ResponseEntity.ok()
                        .body(ApiResponse.<List<Booking>>builder()
                                .isSuccess(false)
                                .message("No bookings found ")
                                .response(bookings)
                                .build());
            }

            log.info("Found {} bookings ", bookings.size());
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(true)
                            .message(bookings.size() + " booking(s) found")
                            .response(bookings)
                            .build());
        } catch (Exception e) {
            log.error("Error fetching bookings {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Booking>>builder()
                            .isSuccess(false)
                            .message("Failed to retrieve bookings: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

}