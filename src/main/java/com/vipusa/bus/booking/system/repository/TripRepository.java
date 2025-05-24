package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
