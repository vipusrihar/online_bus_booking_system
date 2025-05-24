package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
}
