package com.vipusa.bus.booking.system.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserRequest {
    private String name;
    private String email;
    private String password;
}
