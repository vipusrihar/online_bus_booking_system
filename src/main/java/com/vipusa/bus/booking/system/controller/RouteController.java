package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.entity.Bus;
import com.vipusa.bus.booking.system.entity.Route;
import com.vipusa.bus.booking.system.request.CreateRouteRequest;
import com.vipusa.bus.booking.system.request.EditRouteRequest;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.service.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService){
        this.routeService = routeService;
    }

    @PostMapping("/create")
    ResponseEntity<ApiResponse<Route>> createRoute(@RequestBody @Valid CreateRouteRequest request){
        log.info("Creating new route with request: {}", request);
        try{
            Route route = routeService.createRoute(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(true)
                            .message("Route created successfully")
                            .response(route)
                            .build());
        }catch (Exception e){
            log.error("Error creating route : {}",request);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(false)
                            .message("Error creating Route "+e.getMessage())
                            .response(null)
                            .build());

        }
    }

    @GetMapping("/getAll")
    ResponseEntity<ApiResponse<List<Route>>> getAllRoutes(){
        log.info("Fetching all routes");
        try{
            List<Route> routeList = routeService.getAllRoute();
            if(routeList.isEmpty()){
                log.warn("No Routes Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse
                                .<List<Route>>builder()
                                .isSuccess(false)
                                .response(null)
                                .message("No Routes Available")
                                .build());
            }
            log.info("Successfully retrieved {} routes",routeList.size());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<List<Route>>builder()
                            .isSuccess(true)
                            .response(routeList)
                            .message("Routes "+routeList.size()+" found")
                            .build());
        }catch (Exception e){
            log.error("Error Happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse
                            .<List<Route>>builder()
                            .isSuccess(false)
                            .response(null)
                            .message("Error happened "+e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{routeId}")
    ResponseEntity<ApiResponse<Route>> getRouteById(@PathVariable @Min(1) Long routeId){
        log.info("fetching Route with ID : {}",routeId);
        try {
            Route route = routeService.getRouteById(routeId);
            if(route == null){
                log.warn("No Route found with ID {}",routeId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse
                                .<Route>builder()
                                .isSuccess(false)
                                .response(null)
                                .message("No Route Available with ID "+ routeId)
                                .build());
            }
            log.info("Route successfully retrieve for ID {} ",routeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<Route>builder()
                            .isSuccess(true)
                            .response(route)
                            .message("Route Available with ID "+ routeId)
                            .build());
        }catch (Exception e){
            log.error("Error Happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<Route>builder()
                            .isSuccess(false)
                            .response(null)
                            .message("Error happened : "+e.getMessage())
                            .build());

        }
    }

    @GetMapping("/routeNumber/{routeNumber}")
    public ResponseEntity<ApiResponse<Route>> getRouteByRouteNumber(@PathVariable String routeNumber) {
        log.info("Fetching Route with routeNumber: {}", routeNumber);
        try {
            Route route = routeService.getRoutByRouteNumber(routeNumber);

            log.info("Route successfully retrieved for routeNumber {}", routeNumber);
            return ResponseEntity.ok(
                    ApiResponse.<Route>builder()
                            .isSuccess(true)
                            .response(route)
                            .message("Route available with route number " + routeNumber)
                            .build()
            );
        } catch (RuntimeException e) {
            log.warn("No Route found with routeNumber {}", routeNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(false)
                            .response(null)
                            .message("No Route available with route number " + routeNumber)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(false)
                            .response(null)
                            .message("Error occurred: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/start/{startPlace}")
    ResponseEntity<ApiResponse<List<Route>>> getRouteByStartingLocation(@PathVariable String startPlace){
        log.info("fetching Route which are start at : {}",startPlace);
        try {
            List<Route> routeList = routeService.getRouteByStartLocation(startPlace);
            if(routeList.isEmpty()){
                log.warn("No Route found which are starting at {}",startPlace);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse
                                .<List<Route>>builder()
                                .isSuccess(false)
                                .response(null)
                                .message("No Route Available starting at "+ startPlace)
                                .build());
            }
            log.info("Route successfully retrieve starting at {} ",startPlace);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<List<Route>>builder()
                            .isSuccess(true)
                            .response(routeList)
                            .message(routeList.size()+" Routes Available starting at "+ startPlace)
                            .build());
        }catch (Exception e){
            log.error("Error Happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse
                            .<List<Route>>builder()
                            .isSuccess(false)
                            .response(null)
                            .message("Error happened : "+e.getMessage())
                            .build());

        }
    }

    @GetMapping("/end/{endPlace}")
    ResponseEntity<ApiResponse<List<Route>>> getRouteByEndingLocation(@PathVariable String endPlace){
            log.info("fetching Route which are end at : {}",endPlace);
            try {
                List<Route> routeList = routeService.getRouteByEndLocation(endPlace);
                if(routeList.isEmpty()){
                    log.warn("No Route found which are ending at {}",endPlace);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(ApiResponse
                                    .<List<Route>>builder()
                                    .isSuccess(false)
                                    .response(null)
                                    .message("No Route Available ending at "+ endPlace)
                                    .build());
                }
                log.info("Route successfully retrieve ending at {} ",endPlace);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse
                                .<List<Route>>builder()
                                .isSuccess(true)
                                .response(routeList)
                                .message(routeList.size()+" Routes Available ending at "+ endPlace)
                                .build());
            }catch (Exception e){
                log.error("Error Happened {}",e.getMessage());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse
                                .<List<Route>>builder()
                                .isSuccess(false)
                                .response(null)
                                .message("Error happened : "+e.getMessage())
                                .build());

            }

    }

    @GetMapping("/stopping/{place}")
    ResponseEntity<ApiResponse<List<Route>>> getRoutesByStopping(@PathVariable String place){
        log.info("feting the routes by using route {} ",place);
        try{
            List<Route> routeList = routeService.getRoutesByStopping(place);
            if(routeList.isEmpty()){
                log.warn("No Route found  through {}",place);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ApiResponse.<List<Route>>builder().
                                        isSuccess(false)
                                        .response(null)
                                        .message("No routes found through "+place)
                                        .build());
            }
            log.info("Routes {} found through {}",routeList.size(),place);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.<List<Route>>builder().
                                    isSuccess(true)
                                    .response(routeList)
                                    .message(routeList.size()+" routes found through "+place)
                                    .build());
        }catch (Exception e){
            log.error("Error happened {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            ApiResponse.<List<Route>>builder().
                                    isSuccess(false)
                                    .response(null)
                                    .message("Error happened "+e.getMessage())
                                    .build());
        }
    }

    @PutMapping("/{routeId}")
    ResponseEntity<ApiResponse<Route>> updateRoute
            ( @PathVariable @Min(1) Long routeId,
              @RequestBody @Valid EditRouteRequest request) {

        log.info("Changing the route with the ID: {}", routeId);

        try {
            Route route = routeService.getRouteById(routeId);
            if (route == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<Route>builder()
                                .isSuccess(false)
                                .message("Route not found with ID:"+routeId)
                                .response(null)
                                .build());
            }

            Route updateRoute = routeService.changeRouteDetails(routeId,request);

            return ResponseEntity.ok()
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(true)
                            .message("Route updated successfully")
                            .response(updateRoute)
                            .build());

        } catch (Exception e) {
            log.error("Error updating route with ID: {}", routeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Route>builder()
                            .isSuccess(false)
                            .message("Error updating route: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @DeleteMapping("/{routeId}")
    ResponseEntity<ApiResponse<Boolean>> deleteRoute(@PathVariable @Min(1) Long routeId){
        log.info("trying to delete the route ID with {}",routeId);
        try {
            boolean isDeleted = routeService.deleteRoute(routeId);

            if (isDeleted) {
                log.info("Successfully deleted route with ID: {}", routeId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<Boolean>builder()
                                .isSuccess(true)
                                .message("Route with ID " + routeId + " was successfully deleted")
                                .response(true)
                                .build());
            }

            log.warn("Route with ID {} could not be deleted (might not exist)", routeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Route with ID " + routeId + " could not be found or deleted")
                            .response(false)
                            .build());

        } catch (Exception e) {
            log.error("Error deleting route with ID {}: {}", routeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Failed to delete route: " + e.getMessage())
                            .response(false)
                            .build());
        }
    }

}
