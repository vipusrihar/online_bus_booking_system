package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByStartDateBetween(LocalDate start, LocalDate end);

    List<Trip> findByStartDate(LocalDate startDate);

    List<Trip> findByEndDate(LocalDate localDate);

    List<Trip> findByBus_Id(Long busId);
    List<Trip> findByRoute_Id(Long routeId);
}
