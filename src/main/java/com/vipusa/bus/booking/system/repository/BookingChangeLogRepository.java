package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.BookingChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingChangeLogRepository extends JpaRepository<BookingChangeLog, Long> {
}
