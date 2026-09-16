package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.request.EditRouteRequest;
import com.vipusa.bus.booking.system.service.RouteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createRoute(@RequestBody @Valid CreateRouteRequest request) {
        Route route = routeService.createRoute(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route created successfully")
                .response(route)
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<?>> getAllRoutes() {
        List<Route> routes = routeService.getAllRoute();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All routes retrieved successfully")
                .response(routes)
                .build());
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<ApiResponse<?>> getRouteById(@PathVariable Long routeId) {
        Route route = routeService.getRouteById(routeId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route retrieved successfully")
                .response(route)
                .build());
    }

    @GetMapping("/routeNumber/{routeNumber}")
    public ResponseEntity<ApiResponse<?>> getRouteByRouteNumber(@PathVariable String routeNumber) {
        Route route = routeService.getRouteByRouteNumber(routeNumber);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route retrieved successfully")
                .response(route)
                .build());
    }

    @GetMapping("/start/{startPlace}")
    public ResponseEntity<ApiResponse<?>> getRoutesByStartLocation(@PathVariable String startPlace) {
        List<Route> routes = routeService.getRouteByStartLocation(startPlace);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Routes retrieved successfully")
                .response(routes)
                .build());
    }

    @GetMapping("/end/{endPlace}")
    public ResponseEntity<ApiResponse<?>> getRoutesByEndLocation(@PathVariable String endPlace) {
        List<Route> routes = routeService.getRouteByEndLocation(endPlace);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Routes retrieved successfully")
                .response(routes)
                .build());
    }

    @GetMapping("/stopping/{place}")
    public ResponseEntity<ApiResponse<?>> getRoutesByStopping(@PathVariable String place) {
        List<Route> routes = routeService.getRoutesByStopping(place);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Routes retrieved successfully")
                .response(routes)
                .build());
    }

    @PutMapping("/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateRoute(
            @PathVariable Long routeId,
            @RequestBody @Valid EditRouteRequest request) {
        Route route = routeService.changeRouteDetails(routeId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route updated successfully")
                .response(route)
                .build());
    }

    @DeleteMapping("/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteRoute(@PathVariable Long routeId) {
        boolean result = routeService.deleteRoute(routeId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route deleted successfully")
                .response(result)
                .build());
    }

    // Route stop endpoints
    @PostMapping("/{routeId}/stops")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addStopToRoute(
            @PathVariable Long routeId,
            @RequestBody @Valid com.vipusa.bus.booking.system.request.CreateRouteStopRequest request) {
        Route route = routeService.addStopToRoute(routeId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Stop added to route successfully")
                .response(route)
                .build());
    }

    @DeleteMapping("/{routeId}/stops/{stopId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> removeStopFromRoute(
            @PathVariable Long routeId,
            @PathVariable Long stopId) {
        boolean result = routeService.removeStopFromRoute(routeId, stopId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Stop removed from route successfully")
                .response(result)
                .build());
    }
}