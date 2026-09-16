package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.entity.RouteStop;
import com.vipusa.bus.booking.system.exception.RouteNotFoundException;
import com.vipusa.bus.booking.system.repository.RouteRepository;
import com.vipusa.bus.booking.system.repository.RouteStopRepository;
import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.request.CreateRouteStopRequest;
import com.vipusa.bus.booking.system.request.EditRouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public RouteServiceImpl(RouteRepository routeRepository, RouteStopRepository routeStopRepository) {
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
    }

    @Override
    @Transactional
    public Route createRoute(CreateRouteRequest request) {
        Route route = new Route();
        route.setRouteNumber(request.getRouteNumber());
        route.setStartLocation(request.getStartLocation());
        route.setEndLocation(request.getEndLocation());
        route.setDistanceKm(request.getDistanceKm());

        Route savedRoute = routeRepository.save(route);

        // Add stops if provided
        if (request.getStops() != null && !request.getStops().isEmpty()) {
            for (CreateRouteStopRequest stopReq : request.getStops()) {
                RouteStop stop = new RouteStop();
                stop.setRoute(savedRoute);
                stop.setStopName(stopReq.getStopName());
                stop.setSequenceOrder(stopReq.getSequenceOrder());
                routeStopRepository.save(stop);
            }
        }

        return savedRoute;
    }

    @Override
    public List<Route> getRoutesByStopping(String place) {
        return routeRepository.findByStops_StopNameIgnoreCase(place);
    }

    @Override
    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("Route Not Found with ID " + routeId));
    }

    @Override
    public List<Route> getAllRoute() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteByRouteNumber(String routeNumber) {
        return routeRepository.findByRouteNumber(routeNumber)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with routeNumber " + routeNumber));
    }

    @Override
    public List<Route> getRouteByStartLocation(String startLocation) {
        return routeRepository.findByStartLocation(startLocation);
    }

    @Override
    public List<Route> getRouteByEndLocation(String endLocation) {
        return routeRepository.findByEndLocation(endLocation);
    }

    @Override
    @Transactional
    public boolean deleteRoute(Long routeId) {
        if (!routeRepository.existsById(routeId)) {
            throw new RouteNotFoundException("Route not found with id: " + routeId);
        }
        routeRepository.deleteById(routeId);
        return true;
    }

    @Override
    @Transactional
    public Route changeRouteDetails(Long routeId, EditRouteRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("Route not found with ID: " + routeId));

        if (request.getRouteNumber() != null) {
            route.setRouteNumber(request.getRouteNumber());
        }
        if (request.getStartLocation() != null) {
            route.setStartLocation(request.getStartLocation());
        }
        if (request.getEndLocation() != null) {
            route.setEndLocation(request.getEndLocation());
        }
        if (request.getDistanceKm() != null) {
            route.setDistanceKm(request.getDistanceKm());
        }

        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public Route addStopToRoute(Long routeId, com.vipusa.bus.booking.system.request.CreateRouteStopRequest stopRequest) {
        Route route = getRouteById(routeId);

        // Check if sequence order already exists
        if (routeStopRepository.findByRoute_IdAndSequenceOrder(routeId, stopRequest.getSequenceOrder()).isPresent()) {
            throw new IllegalArgumentException("Sequence order " + stopRequest.getSequenceOrder() + " already exists for this route");
        }

        RouteStop stop = new RouteStop();
        stop.setRoute(route);
        stop.setStopName(stopRequest.getStopName());
        stop.setSequenceOrder(stopRequest.getSequenceOrder());
        routeStopRepository.save(stop);

        return route;
    }

    @Override
    @Transactional
    public boolean removeStopFromRoute(Long routeId, Long stopId) {
        RouteStop stop = routeStopRepository.findById(stopId)
                .orElseThrow(() -> new IllegalArgumentException("Route stop not found with id: " + stopId));

        if (!stop.getRoute().getId().equals(routeId)) {
            throw new IllegalArgumentException("Stop does not belong to route " + routeId);
        }

        routeStopRepository.delete(stop);
        return true;
    }
}