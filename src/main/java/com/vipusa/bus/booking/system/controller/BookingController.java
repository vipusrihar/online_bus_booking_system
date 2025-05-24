package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.DTO.ApiResponse;
import com.vipusa.bus.booking.system.DTO.CreateBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/{userId}/create")
    public ResponseEntity<ApiResponse<?>> createBooking(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid CreateBookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(userId,bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .builder()
                        .response(booking)
                        .message("Booking successfully Created")
                        .isSuccess(true)
                        .build());
    }

    @PutMapping("/{userId}/{tripId}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelBooking(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long tripId) {

        boolean isCancelled = bookingService.cancelBooking(userId, tripId);

        if (!isCancelled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse
                            .builder()
                            .response(null)
                            .message("Failed to cancel booking - booking might not exist or cancellation period expired")
                            .isSuccess(false)
                            .build());
        }

        return ResponseEntity.ok()
                .body(ApiResponse
                        .builder()
                        .response(true)
                        .message("Booking successfully cancelled")
                        .isSuccess(true)
                        .build());
    }

    @GetMapping("/{userId}/allBookings")
    public ResponseEntity<ApiResponse<?>> getAllBookingOfUser(
            @PathVariable @Min(1) Long userId) {

        try {
            List<Booking> bookings = bookingService.getAllBookingByUserId(userId);

            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .isSuccess(false)
                                .message("No bookings found for user ID: " + userId)
                                .response(null)
                                .build());
            }

            return ResponseEntity.ok()
                    .body(ApiResponse.builder()
                            .isSuccess(true)
                            .message("Successfully retrieved bookings")
                            .response(bookings)
                            .build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .isSuccess(false)
                            .message(e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{userId}/{bookingId}")
    public ResponseEntity<ApiResponse<?>> getBookingById(
            @PathVariable Long userId,
            @PathVariable Long bookingId){
        Booking booking = bookingService.getBookingById(bookingId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .response(booking)
                        .isSuccess(true)
                        .message("")
                        .build());
    }
}
