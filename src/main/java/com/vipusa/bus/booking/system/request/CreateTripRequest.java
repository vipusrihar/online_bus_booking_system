package com.vipusa.bus.booking.system.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
public class CreateTripRequest {
    private Long busId;

    private Long routeId;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;
}
