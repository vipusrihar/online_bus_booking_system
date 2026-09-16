package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.entity.RouteStop;
import com.vipusa.bus.booking.system.request.CreateRouteStopRequest;
import com.vipusa.bus.booking.system.request.EditRouteStopRequest;
import com.vipusa.bus.booking.system.service.RouteStopService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/route-stop")
public class RouteStopController {

    private final RouteStopService routeStopService;

    public RouteStopController(RouteStopService routeStopService) {
        this.routeStopService = routeStopService;
    }

    @PostMapping("/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createRouteStop(
            @PathVariable Long routeId,
            @RequestBody @Valid CreateRouteStopRequest request) {
        RouteStop stop = routeStopService.createRouteStop(routeId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route stop created successfully")
                .response(stop)
                .build());
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<ApiResponse<?>> getRouteStops(@PathVariable Long routeId) {
        List<RouteStop> stops = routeStopService.getRouteStops(routeId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route stops retrieved successfully")
                .response(stops)
                .build());
    }

    @GetMapping("/{stopId}")
    public ResponseEntity<ApiResponse<?>> getRouteStopById(@PathVariable Long stopId) {
        RouteStop stop = routeStopService.getRouteStopById(stopId)
                .orElseThrow(() -> new RuntimeException("Route stop not found with id: " + stopId));
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route stop retrieved successfully")
                .response(stop)
                .build());
    }

    @PutMapping("/{stopId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateRouteStop(
            @PathVariable Long stopId,
            @RequestBody @Valid EditRouteStopRequest request) {
        RouteStop stop = routeStopService.updateRouteStop(stopId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route stop updated successfully")
                .response(stop)
                .build());
    }

    @DeleteMapping("/{stopId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteRouteStop(@PathVariable Long stopId) {
        boolean result = routeStopService.deleteRouteStop(stopId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Route stop deleted successfully")
                .response(result)
                .build());
    }
}