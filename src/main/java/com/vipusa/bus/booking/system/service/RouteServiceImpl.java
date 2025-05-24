package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.entity.Route;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteServiceImpl implements RouteService{
    @Override
    public Route createRoute(CreateRouteRequest request) {
        return null;
    }

    @Override
    public List<String> getStoppingOfRoute(Long routeId) {
        return null;
    }

    @Override
    public Route getRouteById(Long routeId) {
        return null;
    }

    @Override
    public List<Route> getAllRoute() {
        return null;
    }

    @Override
    public List<Route> getRoutByRouteNumber(String routeNumber) {
        return null;
    }

    @Override
    public List<Route> getRouteByStartLocation(String startLocation) {
        return null;
    }

    @Override
    public List<Route> getRouteByEndLocation(String endLocation) {
        return null;
    }

    @Override
    public boolean deleteRoute(Long routeId) {
        return false;
    }
}
