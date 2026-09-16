package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import com.vipusa.bus.booking.system.service.BusService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/bus")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createBus(@RequestBody @Valid CreateBusRequest request) {
        Bus bus = busService.createBus(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bus created successfully")
                .response(bus)
                .build());
    }

    @GetMapping("/{busId}")
    public ResponseEntity<ApiResponse<?>> getBusById(@PathVariable Long busId) {
        Bus bus = busService.getBusById(busId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bus retrieved successfully")
                .response(bus)
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<?>> getAllBuses() {
        List<Bus> buses = busService.getAllBus();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All buses retrieved successfully")
                .response(buses)
                .build());
    }

    @GetMapping("/byBusType/{busType}")
    public ResponseEntity<ApiResponse<?>> getBusesByBusType(@PathVariable BUS_TYPE busType) {
        List<Bus> buses = busService.getBusesByBusType(busType);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Buses retrieved successfully")
                .response(buses)
                .build());
    }

    @GetMapping("/byDepot/{depotId}")
    public ResponseEntity<ApiResponse<?>> getBusesByDepot(@PathVariable Long depotId) {
        List<Bus> buses = busService.getBusesByDepot(depotId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Buses retrieved successfully")
                .response(buses)
                .build());
    }

    @PutMapping("/editBus/{busId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateBus(
            @PathVariable Long busId,
            @RequestBody @Valid EditBusRequest request) {
        Bus bus = busService.changeBusDetails(busId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bus updated successfully")
                .response(bus)
                .build());
    }

    @DeleteMapping("/{busId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteBus(@PathVariable Long busId) {
        boolean result = busService.deleteBus(busId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bus deleted successfully")
                .response(result)
                .build());
    }
}