package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.defaults.BookingStatus;

import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, CreateBookingRequest bookingRequest);
    boolean cancelBooking(Long userId, Long bookingId);
    Booking getBookingById(Long bookingId);
    List<Booking> getAllActiveBookingByUserId(Long userId);
    List<Booking> getAllBookingByTripId(Long tripId);
    Booking changeBooking(Long userId, Long bookingId, EditBookingRequest request);
    List<Booking> getAllBooking();
    List<Booking> getAllCanceledBookingsByUserId(Long userId);
    List<Booking> getAllHoldBookingByUserId(Long userId);
    List<Booking> getAllChangedBookingByUserId(Long userId);
    List<Booking> getAllBookingByUserId(Long userId);
    Booking holdBooking(Long userId, Long bookingId, int minutes);
    Booking releaseHold(Long bookingId);
}