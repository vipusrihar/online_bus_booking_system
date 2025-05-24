package com.vipusa.bus.booking.system.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T>{

    private boolean isSuccess;

    private  String message;

    private T response;
}
