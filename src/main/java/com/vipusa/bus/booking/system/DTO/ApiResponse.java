package com.vipusa.bus.booking.system.DTO;

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
