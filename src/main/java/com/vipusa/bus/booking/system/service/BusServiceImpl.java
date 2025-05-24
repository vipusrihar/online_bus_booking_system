package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.CreateBusRequest;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusServiceImpl implements BusService{
    @Override
    public Bus createBus(CreateBusRequest createBusRequest) {
        return null;
    }

    @Override
    public Bus getBusById(Long busId) {
        return null;
    }

    @Override
    public List<Bus> getAllBus() {
        return null;
    }

    @Override
    public List<Bus> getBusesByDepots(DEPOTS depots) {
        return null;
    }

    @Override
    public List<Bus> getBusesByBusTypes(BUS_TYPE busType) {
        return null;
    }

    @Override
    public Bus changeBusDetails(Long busId) {
        return null;
    }

    @Override
    public boolean deleteBus(Long busId) {
        return false;
    }
}
