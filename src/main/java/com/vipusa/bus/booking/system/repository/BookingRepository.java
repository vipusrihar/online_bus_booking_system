package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import com.vipusa.bus.booking.system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);

    Optional<Booking> findByIdAndUser_Id(Long bookingId, Long userId);

    List<Booking> findByUser_IdAndStatus(Long userId, BookingStatus bookingStatus);

    List<Booking> findByTrip_Id(Long tripId);
}
