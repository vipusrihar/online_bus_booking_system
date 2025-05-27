package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.repository.RouteRepository;
import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.request.EditRouteRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RouteServiceImpl implements RouteService{

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }
    @Override
    public Route createRoute(CreateRouteRequest request) {
        Route route = new Route();
        route.setRouteNumber(request.getRouteNumber());
        route.setDistance(request.getDistance());
        route.setStartLocation(request.getStartLocation());
        route.setEndLocation(request.getEndLocation());
        route.setStoppingPlaces(request.getStoppingPlaces());

        Route savedRoute = routeRepository.save(route);
        return savedRoute;
    }

    @Override
    public List<Route> getRoutesByStopping(String place) {
        List<Route> routeList = routeRepository.findAll();
        List<Route> matchedRoutes = new ArrayList<>();

        for (Route route : routeList) {
            for (String stop : route.getStoppingPlaces()) {
                if (stop.equalsIgnoreCase(place)) {
                    matchedRoutes.add(route);
                    break;
                }
            }
        }
        return matchedRoutes;
    }


    @Override
    public Route getRouteById(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow( () -> new RuntimeException("Route Not Found with ID "+ routeId));
        return route;
    }

    @Override
    public List<Route> getAllRoute() {
        List<Route> routeList = routeRepository.findAll();
        return routeList;
    }

    @Override
    public  Route getRoutByRouteNumber(String routeNumber) {
        Route route = routeRepository.getRouteByRouteNumber(routeNumber).orElseThrow(() -> new RuntimeException("Route not found with routeNumber" + routeNumber));
        return route;
    }

    @Override
    public List<Route> getRouteByStartLocation(String startLocation) {
        List<Route> routeList = routeRepository.getRouteByStartingLocation(startLocation);
        return routeList;
    }

    @Override
    public List<Route> getRouteByEndLocation(String endLocation) {
        List<Route> routeList = routeRepository.getRouteByEndingLocation(endLocation);
        return routeList;
    }

    @Override
    public boolean deleteRoute(Long routeId) {
        if (!routeRepository.existsById(routeId)) {
            throw new EntityNotFoundException("Route not found with id: " + routeId);
        }
        routeRepository.deleteById(routeId);
        return true;
    }

    @Override
    public Route changeRouteDetails(Long routeId, EditRouteRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with ID: " + routeId));

        if (request.getRouteNumber() != null) {
            route.setRouteNumber(request.getRouteNumber());
        }
        if (request.getDistance() != null) {
            route.setDistance(request.getDistance());
        }
        if (request.getStartLocation() != null) {
            route.setStartLocation(request.getStartLocation());
        }
        if (request.getEndLocation() != null) {
            route.setEndLocation(request.getEndLocation());
        }
        if (request.getStoppingPlaces() != null && !request.getStoppingPlaces().isEmpty()) {
            route.setStoppingPlaces(request.getStoppingPlaces());
        }

        return routeRepository.save(route);
    }

}
