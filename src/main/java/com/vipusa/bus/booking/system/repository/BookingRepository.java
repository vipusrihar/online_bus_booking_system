package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.defaults.BookingStatus;
import com.vipusa.bus.booking.system.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_IdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByTrip_Id(Long tripId);
    Optional<Booking> findByIdAndUser_Id(Long bookingId, Long userId);
    Optional<Booking> findByBookingRef(String bookingRef);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdWithLock(@Param("id") Long id);
}