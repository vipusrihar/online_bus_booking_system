package com.vipusa.bus.booking.system.request;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBusRequest {

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    @NotNull(message = "Bus type is required")
    private BUS_TYPE busType;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Depot is required")
    private Long depotId;
}