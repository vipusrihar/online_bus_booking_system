package com.vipusa.bus.booking.system;

import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Depot;
import com.vipusa.bus.booking.system.repository.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DepotDataSeeder {

    @Autowired
    private DepotRepository depotRepository;

    @EventListener
    @Transactional
    public void loadDepots(ContextRefreshedEvent event) {
        for (DEPOTS depot : DEPOTS.values()) {
            if (!depotRepository.existsByName(depot.name())) {
                depotRepository.save(new Depot(depot.name(), depot.name()));
            }
        }
    }
}