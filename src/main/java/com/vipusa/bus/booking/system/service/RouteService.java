package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.entity.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteService {

    Route createRoute(CreateRouteRequest request);

    List<String> getStoppingOfRoute(Long routeId);

    Route getRouteById(Long routeId);

    List<Route> getAllRoute();

    List<Route> getRoutByRouteNumber(String routeNumber);

    List<Route> getRouteByStartLocation(String startLocation);

    List<Route> getRouteByEndLocation(String endLocation);

    boolean deleteRoute(Long routeId);
}
