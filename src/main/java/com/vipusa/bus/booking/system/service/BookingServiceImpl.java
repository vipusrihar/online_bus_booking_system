package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.EditBookingRequest;
import com.vipusa.bus.booking.system.DTO.CreateBookingRequest;
import com.vipusa.bus.booking.system.entity.Booking;
import com.vipusa.bus.booking.system.repository.BookingRepository;
import com.vipusa.bus.booking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingServiceImpl implements BookingService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Long userId, CreateBookingRequest bookingRequest) {
//        Booking booking = new Booking();
//        User user = userRepository.findById(userId);
//        booking.setUser(user);
//        booking.setTrip();
        return null;
    }

    @Override
    public boolean cancelBooking(Long userId, Long bookingId) {
        return false;
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingByTripId(Long tripId) {
        return null;
    }

    @Override
    public Booking changeBooking(Long bookId, EditBookingRequest request) {
        return null;
    }
}
