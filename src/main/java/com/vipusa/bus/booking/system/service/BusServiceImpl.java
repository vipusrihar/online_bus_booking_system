package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.entity.Depot;
import com.vipusa.bus.booking.system.exception.BusNotFoundException;
import com.vipusa.bus.booking.system.exception.DepotNotFoundException;
import com.vipusa.bus.booking.system.repository.BusRepository;
import com.vipusa.bus.booking.system.repository.DepotRepository;
import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final DepotRepository depotRepository;

    public BusServiceImpl(BusRepository busRepository, DepotRepository depotRepository) {
        this.busRepository = busRepository;
        this.depotRepository = depotRepository;
    }

    @Override
    @Transactional
    public Bus createBus(CreateBusRequest request) {
        Depot depot = depotRepository.findById(request.getDepotId())
                .orElseThrow(() -> new DepotNotFoundException("Depot not found with id: " + request.getDepotId()));

        Bus bus = new Bus();
        bus.setBusNumber(request.getBusNumber());
        bus.setBusType(request.getBusType());
        bus.setTotalSeats(request.getTotalSeats());
        bus.setDepot(depot);
        return busRepository.save(bus);
    }

    @Override
    public Bus getBusById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found with id: " + busId));
    }

    @Override
    public List<Bus> getAllBus() {
        return busRepository.findAll();
    }

    @Override
    public List<Bus> getBusesByBusType(BUS_TYPE busType) {
        return busRepository.findByBusType(busType);
    }

    @Override
    public List<Bus> getBusesByDepot(Long depotId) {
        return busRepository.findByDepot_Id(depotId);
    }

    @Override
    @Transactional
    public Bus changeBusDetails(Long busId, EditBusRequest request) {
        Bus bus = getBusById(busId);

        if (request.getBusNumber() != null) {
            bus.setBusNumber(request.getBusNumber());
        }
        if (request.getBusType() != null) {
            bus.setBusType(request.getBusType());
        }
        if (request.getDepotId() != null) {
            Depot depot = depotRepository.findById(request.getDepotId())
                    .orElseThrow(() -> new DepotNotFoundException("Depot not found with id: " + request.getDepotId()));
            bus.setDepot(depot);
        }
        if (request.getTotalSeats() != null) {
            bus.setTotalSeats(request.getTotalSeats());
        }

        return busRepository.save(bus);
    }

    @Override
    @Transactional
    public boolean deleteBus(Long busId) {
        if (!busRepository.existsById(busId)) {
            throw new BusNotFoundException("Bus not found with id: " + busId);
        }
        busRepository.deleteById(busId);
        return true;
    }
}