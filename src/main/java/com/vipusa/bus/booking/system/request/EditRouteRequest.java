package com.vipusa.bus.booking.system.request;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class EditRouteRequest {
    private String startLocation;
    private String endLocation;
    private String routeNumber;
    private Long distance;
    private List<String> stoppingPlaces;

}
