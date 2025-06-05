package com.vipusa.bus.booking.system.request;

import com.vipusa.bus.booking.system.entity.Trip;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "Trip is important")
    private Long tripId;

    @NotEmpty(message = "At least one seat ")
    private List<Integer> seatNumbers;

}
