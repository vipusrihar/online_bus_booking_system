package com.vipusa.bus.booking.system.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T response;

    public static <T> ApiResponse<T> success( String message, T response ) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .response(response)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .response(null)
                .build();
    }
}