package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.BookingChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingChangeLogRepository extends JpaRepository<BookingChangeLog, Long> {
    List<BookingChangeLog> findByOldBooking_Id(Long bookingId);
    List<BookingChangeLog> findByNewBooking_Id(Long bookingId);
    List<BookingChangeLog> findByOldBooking_User_Id(Long userId);
}