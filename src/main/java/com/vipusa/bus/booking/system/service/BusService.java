package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BusService {

    Bus createBus(CreateBusRequest createBusRequest);

    Bus getBusById(Long busId);

    List<Bus> getAllBus();

    List<Bus> getBusesByDepots(DEPOTS depots);

    List<Bus> getBusesByBusTypes(BUS_TYPE busType);

    Bus changeBusDetails(Long busId);

    boolean deleteBus(Long busId);
}
