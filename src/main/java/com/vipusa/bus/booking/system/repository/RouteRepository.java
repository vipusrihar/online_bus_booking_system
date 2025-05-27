package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r FROM Route r WHERE r.routeNumber = :routeNumber")
    Optional<Route> getRouteByRouteNumber(@Param("routeNumber") String routeNumber);

    @Query("SELECT r FROM Route r WHERE r.startLocation = :location")
    List<Route> getRouteByStartingLocation(@Param("location") String startLocation);

    @Query("SELECT r FROM Route r WHERE r.endLocation = :location")
    List<Route> getRouteByEndingLocation(@Param("location") String endLocation);
}
