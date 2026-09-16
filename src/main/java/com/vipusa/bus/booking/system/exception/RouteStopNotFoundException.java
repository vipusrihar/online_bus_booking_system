package com.vipusa.bus.booking.system.exception;

public class RouteStopNotFoundException extends RuntimeException {
    public RouteStopNotFoundException(String message) {
        super(message);
    }
}