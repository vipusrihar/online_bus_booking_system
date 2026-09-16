package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;

import java.util.List;

public interface BusService {
    Bus createBus(CreateBusRequest request);
    Bus getBusById(Long busId);
    List<Bus> getAllBus();
    List<Bus> getBusesByBusType(BUS_TYPE busType);
    List<Bus> getBusesByDepot(Long depotId);
    Bus changeBusDetails(Long busId, EditBusRequest request);
    boolean deleteBus(Long busId);
}