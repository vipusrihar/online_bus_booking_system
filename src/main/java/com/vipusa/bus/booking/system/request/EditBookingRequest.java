package com.vipusa.bus.booking.system.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditBookingRequest {

    @NotNull(message = "Trip is important")
    private Long tripId;

    @NotEmpty(message = "At least one seat is required")
    private List<@Min(value = 1, message = "Seat number must be at least 1") Integer> seatNumbers;
}