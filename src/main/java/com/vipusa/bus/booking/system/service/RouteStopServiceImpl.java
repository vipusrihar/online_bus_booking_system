package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.entity.RouteStop;
import com.vipusa.bus.booking.system.exception.RouteNotFoundException;
import com.vipusa.bus.booking.system.exception.RouteStopNotFoundException;
import com.vipusa.bus.booking.system.repository.RouteRepository;
import com.vipusa.bus.booking.system.repository.RouteStopRepository;
import com.vipusa.bus.booking.system.request.CreateRouteStopRequest;
import com.vipusa.bus.booking.system.request.EditRouteStopRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RouteStopServiceImpl implements RouteStopService {

    private final RouteStopRepository routeStopRepository;
    private final RouteRepository routeRepository;

    public RouteStopServiceImpl(RouteStopRepository routeStopRepository, RouteRepository routeRepository) {
        this.routeStopRepository = routeStopRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional
    public RouteStop createRouteStop(Long routeId, CreateRouteStopRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with id: " + routeId));

        if (routeStopRepository.findByRoute_IdAndSequenceOrder(routeId, request.getSequenceOrder()).isPresent()) {
            throw new IllegalArgumentException("Sequence order " + request.getSequenceOrder() + " already exists for this route");
        }

        RouteStop stop = new RouteStop();
        stop.setRoute(route);
        stop.setStopName(request.getStopName());
        stop.setSequenceOrder(request.getSequenceOrder());
        return routeStopRepository.save(stop);
    }

    @Override
    @Transactional
    public RouteStop updateRouteStop(Long stopId, EditRouteStopRequest request) {
        RouteStop stop = routeStopRepository.findById(stopId)
                .orElseThrow(() -> new RouteStopNotFoundException("Route stop not found with id: " + stopId));

        if (request.getStopName() != null) {
            stop.setStopName(request.getStopName());
        }
        if (request.getSequenceOrder() != null) {
            if (routeStopRepository.findByRoute_IdAndSequenceOrder(stop.getRoute().getId(), request.getSequenceOrder())
                    .filter(s -> !s.getId().equals(stopId))
                    .isPresent()) {
                throw new IllegalArgumentException("Sequence order " + request.getSequenceOrder() + " already exists for this route");
            }
            stop.setSequenceOrder(request.getSequenceOrder());
        }

        return routeStopRepository.save(stop);
    }

    @Override
    @Transactional
    public boolean deleteRouteStop(Long stopId) {
        if (!routeStopRepository.existsById(stopId)) {
            throw new RouteStopNotFoundException("Route stop not found with id: " + stopId);
        }
        routeStopRepository.deleteById(stopId);
        return true;
    }

    @Override
    public List<RouteStop> getRouteStops(Long routeId) {
        return routeStopRepository.findByRoute_Id(routeId);
    }

    @Override
    public Optional<RouteStop> getRouteStopById(Long stopId) {
        return routeStopRepository.findById(stopId);
    }
}