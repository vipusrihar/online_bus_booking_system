package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
