package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByRouteNumber(String routeNumber);
    List<Route> findByStartLocation(String startLocation);
    List<Route> findByEndLocation(String endLocation);
    List<Route> findByStops_StopNameIgnoreCase(String stopName);
}