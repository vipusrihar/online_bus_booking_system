package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Trip;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByBus_Id(Long busId);
    List<Trip> findByRoute_Id(Long routeId);
    
    @Query("SELECT t FROM Trip t WHERE DATE(t.departsAt) = :date")
    List<Trip> findByStartDate(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Trip t WHERE DATE(t.arrivesAt) = :date")
    List<Trip> findByEndDate(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Trip t WHERE DATE(t.departsAt) BETWEEN :start AND :end")
    List<Trip> findByStartDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trip t WHERE t.id = :id")
    Optional<Trip> findByIdWithLock(@Param("id") Long id);
}