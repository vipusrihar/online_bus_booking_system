package com.vipusa.bus.booking.system.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRouteStopRequest {

    private String stopName;

    @Min(value = 0, message = "Sequence order must be zero or greater")
    private Integer sequenceOrder;
}