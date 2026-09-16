package com.vipusa.bus.booking.system.response;

import com.vipusa.bus.booking.system.defaults.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private Integer seatNumber;
    private SeatStatus status;
}