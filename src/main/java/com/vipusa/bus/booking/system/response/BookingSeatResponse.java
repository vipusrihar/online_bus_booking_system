package com.vipusa.bus.booking.system.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponse {
    private Long seatId;
    private Integer seatNumber;
    private String seatStatus;
}