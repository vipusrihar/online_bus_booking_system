package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.RouteStop;
import com.vipusa.bus.booking.system.request.CreateRouteStopRequest;
import com.vipusa.bus.booking.system.request.EditRouteStopRequest;

import java.util.List;
import java.util.Optional;

public interface RouteStopService {
    RouteStop createRouteStop(Long routeId, CreateRouteStopRequest request);
    RouteStop updateRouteStop(Long stopId, EditRouteStopRequest request);
    boolean deleteRouteStop(Long stopId);
    List<RouteStop> getRouteStops(Long routeId);
    Optional<RouteStop> getRouteStopById(Long stopId);
}