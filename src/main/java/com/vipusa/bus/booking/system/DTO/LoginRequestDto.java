package com.vipusa.bus.booking.system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Email(message = "Email not in a valid form")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "password is must")
    @Size(min = 4, message = "password length higher than 4")
    @Size(max = 10, message = "password length lower than 10")
    private String password;

}
