package com.vipusa.bus.booking.system.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTripRequest {

    @NotNull(message = "Bus is required")
    private Long busId;

    @NotNull(message = "Route is required")
    private Long routeId;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departsAt;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivesAt;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}