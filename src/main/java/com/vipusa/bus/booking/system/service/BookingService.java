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

    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getAllBookingByUserId(Long userId);

    // For Admin
    List<Booking> getAllBookingByTripId(Long tripId);

    Booking changeBooking(Long bookId, EditBookingRequest request);


}
