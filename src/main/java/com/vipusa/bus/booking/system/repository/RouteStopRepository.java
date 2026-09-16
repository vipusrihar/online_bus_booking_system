package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    List<RouteStop> findByRoute_Id(Long routeId);
    Optional<RouteStop> findByRoute_IdAndSequenceOrder(Long routeId, Integer sequenceOrder);
}