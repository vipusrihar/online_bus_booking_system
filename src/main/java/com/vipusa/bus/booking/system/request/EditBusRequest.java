package com.vipusa.bus.booking.system.request;

import com.vipusa.bus.booking.system.defaults.BUS_TYPE;
import com.vipusa.bus.booking.system.defaults.DEPOTS;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EditBusRequest {
    private String busNumber;

    private BUS_TYPE busType;

    private Integer totalSeats;

    private DEPOTS ownedDEPO;
}
