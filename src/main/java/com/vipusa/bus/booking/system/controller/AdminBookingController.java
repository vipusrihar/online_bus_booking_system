package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.DTO.ApiResponse;
import com.vipusa.bus.booking.system.DTO.EditBookingRequest;
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
@RequestMapping("/api/admin/booking")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/allBookings/{tripId}")
    public ResponseEntity<ApiResponse<?>> getAllBookingsByTripId(
            @PathVariable @Min(1) Long tripId) {
        try {
            List<Booking> bookings = bookingService.getAllBookingByTripId(tripId);
            boolean found = !bookings.isEmpty();
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .isSuccess(found)
                    .message(found ? bookings.size() + " Booking(s) Found" : "No Bookings Found For This Trip")
                    .response(bookings)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .isSuccess(false)
                            .message("Error Retrieving Bookings: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/allBookings/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getAllBookingByUserId(
            @PathVariable @Min(1) Long userId) {
        try {
            List<Booking> bookings = bookingService.getAllBookingByUserId(userId);
            boolean found = !bookings.isEmpty();
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .isSuccess(found)
                    .message(found ? bookings.size() + " Booking(s) Found" : "No Bookings Found For This User")
                    .response(bookings)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .isSuccess(false)
                            .message("Error Retrieving Bookings: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }


    @PutMapping("/changeBooking/{userId}")
    public ResponseEntity<ApiResponse<?>> changeBookingByUserId(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid EditBookingRequest changeRequest) {

        try {
            Booking updatedBooking = bookingService.changeBooking(userId, changeRequest);

            return ResponseEntity.ok()
                    .body(ApiResponse.builder()
                            .isSuccess(true)
                            .message("Booking updated successfully")
                            .response(updatedBooking)
                            .build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .isSuccess(false)
                            .message(e.getMessage())
                            .response(null)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .isSuccess(false)
                            .message("Error updating booking: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }
}
