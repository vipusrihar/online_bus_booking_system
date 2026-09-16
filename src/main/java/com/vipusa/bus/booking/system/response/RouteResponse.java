package com.vipusa.bus.booking.system.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String routeNumber;
    private String startLocation;
    private String endLocation;
    private Double distanceKm;
    private List<RouteStopResponse> stops;
}