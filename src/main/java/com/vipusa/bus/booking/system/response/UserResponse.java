package com.vipusa.bus.booking.system.response;

import com.vipusa.bus.booking.system.defaults.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private boolean enabled;
    private Set<USER_ROLE> roles;
    private LocalDateTime createdAt;
}