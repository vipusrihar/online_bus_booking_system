package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.request.CreateBusRequest;
import com.vipusa.bus.booking.system.request.EditBusRequest;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.service.BusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/bus")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService){
        this.busService = busService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Bus>> createBus(
            @RequestBody @Valid CreateBusRequest request) {
        log.info("Creating new bus with request: {}", request);
        try {
            Bus bus = busService.createBus(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(true)
                            .message("Bus created successfully")
                            .response(bus)
                            .build());
        }
        catch (Exception e) {
            log.error("Error creating bus: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(false)
                            .message("Failed to create bus: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{busId}")
    public ResponseEntity<ApiResponse<Bus>> getBusById(
            @PathVariable @Min(1) Long busId) {
        log.info("Fetching bus with ID: {}", busId);
        try {
            Bus bus = busService.getBusById(busId);

            if (bus == null) {
                log.warn("Bus not found with ID: {}", busId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Bus>builder()
                                .isSuccess(false)
                                .message("Bus not found")
                                .response(null)
                                .build());
            }

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(true)
                            .message("Bus retrieved successfully")
                            .response(bus)
                            .build());
        }
        catch (Exception e) {
            log.error("Error fetching bus with ID {}: {}", busId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(false)
                            .message("Failed to fetch bus")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<Bus>>> getAllBus(){
        log.info("Fetching All Buses");
        try {
            List<Bus> busList = busService.getAllBus();
            if(busList.isEmpty()){
                log.warn("No Buses found in database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<List<Bus>>builder()
                                .response(null)
                                .isSuccess(false)
                                .message("No buses available")
                                .build());
            }
            log.info("Successfully retrieved {} buses", busList.size());
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Bus>>builder()
                            .response(busList)
                            .isSuccess(true)
                            .message("Successfully Retrieved "+ busList.size() + " Buses")
                            .build());
        }catch (Exception e){
            log.error("Error fetching all buses : {}" ,e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Bus>>builder()
                            .response(null)
                            .isSuccess(false)
                            .message("Failed to Fetch Bus :"+e.getMessage())
                            .build());
        }
    }

    @GetMapping("/byDepots/{depots}")
    public ResponseEntity<ApiResponse<List<Bus>>> getBusesByBusDepots(@PathVariable DEPOTS depots){
        log.info("Fetching buses with DEPOT : {}", depots);
        try {
            List<Bus> busList = busService.getBusesByDepot(depots);
            if(busList.isEmpty()){
                log.error("No buses found with {} DEPOT",depots);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<List<Bus>>builder()
                                .response(null)
                                .isSuccess(false)
                                .message("No buses found")
                                .build());
            }
            log.info("Successfully retrieved {} buses", busList.size());
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Bus>>builder()
                            .response(busList)
                            .isSuccess(true)
                            .message("Successfully Retrieved "+ busList.size() + " Buses")
                            .build());

        }catch (Exception e){
            log.error("Error fetching all buses : {}" ,e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Bus>>builder()
                            .response(null)
                            .isSuccess(false)
                            .message("Failed to Fetch Bus :"+e.getMessage())
                            .build());
        }
    }

    @GetMapping("/byBusType/{busType}")
    public ResponseEntity<ApiResponse<List<Bus>>> getBusesByBusType(
            @PathVariable BUS_TYPE busType) {

        log.info("Fetching buses of type: {}", busType);

        try {
            List<Bus> buses = busService.getBusesByBusType(busType);

            if (buses.isEmpty()) {
                log.warn("No buses found for type: {}", busType);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<List<Bus>>builder()
                                .isSuccess(false)
                                .message("No buses available of type: " + busType)
                                .response(null)
                                .build());
            }

            log.info("Found {} buses of type {}", buses.size(), busType);
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Bus>>builder()
                            .isSuccess(true)
                            .message(String.format("Successfully retrieved %d buses of type %s",
                                    buses.size(), busType))
                            .response(buses)
                            .build());

        } catch (Exception e) {
            log.error("Error fetching buses of type {}: {}", busType, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Bus>>builder()
                            .isSuccess(false)
                            .message("Failed to fetch buses: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @PutMapping("/editBus/{busId}")
    public ResponseEntity<ApiResponse<Bus>> editBus(
            @PathVariable @Min(1) Long busId,
            @RequestBody @Valid EditBusRequest request) {

        log.info("Changing the bus with the ID: {}", busId);

        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Bus>builder()
                                .isSuccess(false)
                                .message("Bus not found with ID:"+busId)
                                .response(null)
                                .build());
            }

            Bus updatedBus = busService.changeBusDetails(busId,request);

            return ResponseEntity.ok()
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(true)
                            .message("Bus updated successfully")
                            .response(updatedBus)
                            .build());

        } catch (Exception e) {
            log.error("Error updating bus with ID: {}", busId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Bus>builder()
                            .isSuccess(false)
                            .message("Error updating bus: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @DeleteMapping("/{busId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteBus(@PathVariable @Min(1) Long busId) {
        log.info("Attempting to delete bus with ID: {}", busId);
        try {
            boolean isDeleted = busService.deleteBus(busId);

            if (isDeleted) {
                log.info("Successfully deleted bus with ID: {}", busId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<Boolean>builder()
                                .isSuccess(true)
                                .message("Bus with ID " + busId + " was successfully deleted")
                                .response(true)
                                .build());
            }

            log.warn("Bus with ID {} could not be deleted (might not exist)", busId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Bus with ID " + busId + " could not be found or deleted")
                            .response(false)
                            .build());

        } catch (Exception e) {
            log.error("Error deleting bus with ID {}: {}", busId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Failed to delete bus: " + e.getMessage())
                            .response(false)
                            .build());
        }
    }
    
}