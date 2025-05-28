package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.entity.Trip;
import com.vipusa.bus.booking.system.request.CreateTripRequest;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.service.TripService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Trip>> createTrip(
            @RequestBody @Valid CreateTripRequest request) {
        log.info("Creating new trip with request: {}", request);
        try {
            Trip trip = tripService.createTrip(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Trip>builder()
                            .isSuccess(true)
                            .message("Trip created successfully")
                            .response(trip)
                            .build());
        }
        catch (Exception e) {
            log.error("Error creating trip: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Trip>builder()
                            .isSuccess(false)
                            .message("Failed to create trip: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Trip>> getTripById(@PathVariable @Min(1) Long tripId) {
        log.info("Fetching trip with ID: {}", tripId);

        try {
            Trip trip = tripService.findTripById(tripId)
                    .orElseThrow(() -> {
                        log.warn("Trip not found with ID: {}", tripId);
                        return new EntityNotFoundException("Trip not found with ID: " + tripId);
                    });

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(ApiResponse.<Trip>builder()
                            .isSuccess(true)
                            .message("Trip retrieved successfully")
                            .response(trip)
                            .build());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Trip>builder()
                            .isSuccess(false)
                            .message(e.getMessage())
                            .response(null)
                            .build());

        } catch (Exception e) {
            log.error("Error fetching trip with ID {}: {}", tripId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Trip>builder()
                            .isSuccess(false)
                            .message("Failed to fetch trip")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTrips() {
        log.info("Fetching all trips");

        try {
            List<Trip> tripList = tripService.getAllTrips();

            if (tripList == null || tripList.isEmpty()) {
                log.warn("No trips found in database");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ApiResponse.<List<Trip>>builder()
                                .response(Collections.emptyList())
                                .isSuccess(false)
                                .message("No trips available")
                                .build());
            }

            log.info("Successfully retrieved {} trips", tripList.size());
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<Trip>>builder()
                            .response(tripList)
                            .isSuccess(true)
                            .message("Successfully retrieved " + tripList.size() + " trips")
                            .build());

        } catch (Exception e) {
            log.error("Error fetching all trips: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<Trip>>builder()
                            .response(null)
                            .isSuccess(false)
                            .message("Failed to fetch trips: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteTrip(@PathVariable @Min(1) Long tripId) {
        log.info("Attempting to delete trip with ID: {}", tripId);
        try {
            boolean isDeleted = tripService.deleteTrip(tripId);

            if (isDeleted) {
                log.info("Successfully deleted trip with ID: {}", tripId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<Boolean>builder()
                                .isSuccess(true)
                                .message("Trip with ID " + tripId + " was successfully deleted")
                                .response(true)
                                .build());
            }

            log.warn("Trip with ID {} could not be deleted (might not exist)", tripId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Bus with ID " + tripId + " could not be found or deleted")
                            .response(false)
                            .build());

        } catch (Exception e) {
            log.error("Error deleting trip with ID {}: {}", tripId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Failed to delete trip: " + e.getMessage())
                            .response(false)
                            .build());
        }
    }

    @GetMapping("/start/{data}")
    public ResponseEntity<ApiResponse<List<Trip>>> getTripStaringAtASpecificDay(@PathVariable String date){
        log.info("Fetching the trip starting at {}",date);
        try {
            List<Trip> tripList = tripService.getTripStartingAtASpecificDay(date);
            if(tripList == null || tripList.isEmpty()){
                log.warn("No Trip found stating at {}",date);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse
                                .<List<Trip>>builder()
                                .isSuccess(false)
                                .message("No trip found starting at this date : "+date)
                                .response(null)
                                .build());
            }
            log.info("{} Trip found stating at {} ",tripList.size(),date);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(true)
                            .message(tripList.size()+" trip found starting at this date : "+date)
                            .response(tripList)
                            .build());
        }catch (Exception e){
            log.error("Error happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(false)
                            .message("Error happened "+e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/end/{data}")
    public ResponseEntity<ApiResponse<List<Trip>>> getTripEndingAtASpecificDay(@PathVariable String date){
        log.info("Fetching the trip ending at {}",date);
        try {
            List<Trip> tripList = tripService.getTripEndingAtASpecificDay(date);
            if(tripList == null || tripList.isEmpty()){
                log.warn("No Trip found ending at {}",date);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse
                                .<List<Trip>>builder()
                                .isSuccess(false)
                                .message("No trip found ending at this date : "+date)
                                .response(null)
                                .build());
            }
            log.info("{} Trip found ending at {} ",tripList.size(),date);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(true)
                            .message(tripList.size()+" trip found ending at this date : "+date)
                            .response(tripList)
                            .build());
        }catch (Exception e){
            log.error("Error happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(false)
                            .message("Error happened "+e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTripSpecificBus(@PathVariable Long busId){
        log.info("Fetching the trip assigned to the busID {}",busId);
        try {
            List<Trip> tripList = tripService.getAllTripSpecificBus(busId);
            if(tripList == null || tripList.isEmpty()){
                log.warn("No Trip found assigned to the busID {}",busId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse
                                .<List<Trip>>builder()
                                .isSuccess(false)
                                .message("No trip found assigned to the busID : "+busId)
                                .response(null)
                                .build());
            }
            log.info("{} Trip found assigned to the busID {} ",tripList.size(),busId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(true)
                            .message(tripList.size()+" trip found assigned to the busID : "+busId)
                            .response(tripList)
                            .build());
        }catch (Exception e){
            log.error("Error happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(false)
                            .message("Error happened "+e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTripSpecificRoute(@PathVariable Long routeId){
        log.info("Fetching the trip assigned to the routeId {}",routeId);
        try {
            List<Trip> tripList = tripService.getAllTripSpecificRoute(routeId);
            if(tripList == null){
                log.warn("No Trip found assigned to the routeId {}",routeId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse
                                .<List<Trip>>builder()
                                .isSuccess(false)
                                .message("No trip found assigned to the routeId  : "+routeId)
                                .response(null)
                                .build());
            }
            log.info("{} Trip found assigned to the routeID {} ",tripList.size(),routeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(true)
                            .message(tripList.size()+" trip found assigned to the routeId : "+routeId)
                            .response(tripList)
                            .build());
        }catch (Exception e){
            log.error("Error happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .<List<Trip>>builder()
                            .isSuccess(false)
                            .message("Error happened "+e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/time")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTripWithinSpecificTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        try {
            List<Trip> trips = tripService.getAllTripStartTimeBetween(startTime, endTime);
            return ResponseEntity.ok(
                    ApiResponse.<List<Trip>>builder()
                            .isSuccess(true)
                            .message("Trips fetched successfully")
                            .response(trips)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error happened {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<List<Trip>>builder()
                            .isSuccess(false)
                            .message("Error happened: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

}
