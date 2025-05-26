package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BusService {

    Bus createBus(CreateBusRequest request) throws Exception;

    Bus getBusById(Long busId);

    List<Bus> getAllBus();

    List<Bus> getBusesByDepot(DEPOTS depots);

    List<Bus> getBusesByBusType(BUS_TYPE busType);

    Bus changeBusDetails(Long busId, EditBusRequest request);

    boolean deleteBus(Long busId);
}
