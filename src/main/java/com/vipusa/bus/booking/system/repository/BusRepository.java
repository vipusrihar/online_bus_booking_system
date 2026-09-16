package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByBusNumber(String busNumber);
    List<Bus> findByBusType(BUS_TYPE busType);
    List<Bus> findByDepot_Id(Long depotId);
}