package com.vipusa.bus.booking.system.request;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditBusRequest {

    private String busNumber;

    private BUS_TYPE busType;

    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    private Long depotId;
}