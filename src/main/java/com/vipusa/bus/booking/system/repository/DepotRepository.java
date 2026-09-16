package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    boolean existsByName(String name);
}