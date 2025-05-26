package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.repository.BusRepository;
import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public Bus createBus(CreateBusRequest request) throws Exception {
        Bus bus = new Bus();
        bus.setBusNumber(request.getBusNumber());
        bus.setBusType(request.getBusType());
        bus.setOwnedDEPO(request.getOwnedDEPO());
        bus.setTotalSeats(request.getTotalSeats());
        return busRepository.save(bus);
    }

    @Override
    public Bus getBusById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found with id: " + busId));
    }

    @Override
    public List<Bus> getAllBus() {
        return busRepository.findAll();
    }

    @Override
    public List<Bus> getBusesByDepot(DEPOTS depots) {
        return busRepository.findByOwnedDEPO(depots);
    }

    @Override
    public List<Bus> getBusesByBusType(BUS_TYPE busType) {
        return busRepository.findByBusType(busType);
    }

    @Override
    public Bus changeBusDetails(Long busId, EditBusRequest request) {
        Bus bus = getBusById(busId); // Reusing the method that includes not-found check

        if (request.getBusNumber() != null) {
            bus.setBusNumber(request.getBusNumber());
        }
        if (request.getBusType() != null) {
            bus.setBusType(request.getBusType());
        }
        if (request.getOwnedDEPO() != null) {
            bus.setOwnedDEPO(request.getOwnedDEPO());
        }
        if (request.getTotalSeats() != null) {
            bus.setTotalSeats(request.getTotalSeats());
        }

        return busRepository.save(bus);
    }

    @Override
    public boolean deleteBus(Long busId) {
        if (!busRepository.existsById(busId)) {
            throw new EntityNotFoundException("Bus not found with id: " + busId);
        }
        busRepository.deleteById(busId);
        return true;
    }
}