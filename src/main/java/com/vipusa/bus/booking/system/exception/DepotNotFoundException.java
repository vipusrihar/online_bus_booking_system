package com.vipusa.bus.booking.system.exception;

public class DepotNotFoundException extends RuntimeException {
    public DepotNotFoundException(String message) {
        super(message);
    }
}