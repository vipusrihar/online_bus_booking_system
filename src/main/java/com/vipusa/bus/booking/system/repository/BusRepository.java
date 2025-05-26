package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByOwnedDEPO(DEPOTS depots);

    List<Bus> findByBusType(BUS_TYPE busType);
}
