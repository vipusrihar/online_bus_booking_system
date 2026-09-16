package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.defaults.SeatStatus;
import com.vipusa.bus.booking.system.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTrip_Id(Long tripId);
    Optional<Seat> findByTrip_IdAndSeatNumber(Long tripId, Integer seatNumber);
    List<Seat> findByTrip_IdAndStatus(Long tripId, SeatStatus status);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findByIdWithLock(@Param("id") Long id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.trip.id = :tripId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByTripAndSeatNumberWithLock(@Param("tripId") Long tripId, @Param("seatNumber") Integer seatNumber);
}