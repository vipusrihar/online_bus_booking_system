package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.BookingSeat;
import com.vipusa.bus.booking.system.entity.BookingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, BookingSeatId> {
    List<BookingSeat> findByBooking_Id(Long bookingId);
    Optional<BookingSeat> findBySeat_Id(Long seatId);
    boolean existsBySeat_Id(Long seatId);
}