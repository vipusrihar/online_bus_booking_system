package com.vipusa.bus.booking.system.request;

import com.vipusa.bus.booking.system.entity.Trip;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Trip is important")
    private Trip trip;

    @NotBlank(message = "At least one seat ")
    private List<Integer> seatNumbers;

}
