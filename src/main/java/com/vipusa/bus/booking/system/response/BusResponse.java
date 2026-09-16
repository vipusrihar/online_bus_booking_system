package com.vipusa.bus.booking.system.response;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusResponse {
    private Long id;
    private String busNumber;
    private BUS_TYPE busType;
    private Integer totalSeats;
    private Long depotId;
    private String depotName;
}