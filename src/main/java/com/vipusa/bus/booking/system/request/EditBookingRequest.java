package com.vipusa.bus.booking.system.request;

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

    @NotEmpty(message = "At least one seat ")
    private List<Integer> seatNumbers;
}
