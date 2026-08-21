package com.vipusa.bus.booking.system.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRouteRequest {

    private String routeNumber;

    private String startLocation;

    private String endLocation;

    @Positive(message = "Distance must be positive")
    private Double distanceKm;
}