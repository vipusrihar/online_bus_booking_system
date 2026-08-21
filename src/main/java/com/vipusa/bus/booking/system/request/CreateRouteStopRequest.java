package com.vipusa.bus.booking.system.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRouteStopRequest {

    @NotNull(message = "Route is required")
    private Long routeId;

    @NotBlank(message = "Stop name is required")
    private String stopName;

    @NotNull(message = "Sequence order is required")
    @Min(value = 0, message = "Sequence order must be zero or greater")
    private Integer sequenceOrder;
}