package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.EditBookingRequest;
import com.vipusa.bus.booking.system.request.CreateBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {

    //For Users
    Booking createBooking(Long userId, CreateBookingRequest bookingRequest);

    boolean cancelBooking(Long userId,Long bookingId);

    Booking getBookingById(Long bookingId);

    List<Booking> getAllActiveBookingByUserId(Long userId);

    List<Booking> getAllCanceledBookingsByUserId(Long userId);

    List<Booking> getAllHoldBookingByUserId(Long userId);
    List<Booking> getAllChangedBookingByUserId(Long userId);

    List<Booking> getAllBookingByUserId(Long userId);


    // For Admin
    List<Booking> getAllBookingByTripId(Long tripId);

    Booking changeBooking(Long userId,Long bookId, EditBookingRequest request);

    List<Booking> getAllBooking();


}
