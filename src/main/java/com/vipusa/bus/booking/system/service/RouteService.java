package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.request.EditRouteRequest;

import java.util.List;

public interface RouteService {
    Route createRoute(CreateRouteRequest request);
    List<Route> getRoutesByStopping(String place);
    Route getRouteById(Long routeId);
    List<Route> getAllRoute();
    Route getRouteByRouteNumber(String routeNumber);
    List<Route> getRouteByStartLocation(String startLocation);
    List<Route> getRouteByEndLocation(String endLocation);
    boolean deleteRoute(Long routeId);
    Route changeRouteDetails(Long routeId, EditRouteRequest request);
    Route addStopToRoute(Long routeId, com.vipusa.bus.booking.system.request.CreateRouteStopRequest stopRequest);
    boolean removeStopFromRoute(Long routeId, Long stopId);
}