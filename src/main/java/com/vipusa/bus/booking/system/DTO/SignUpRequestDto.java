package com.vipusa.bus.booking.system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank(message = "UserName is required")
    @Size(min = 3, message = "UserName should have at least 3 character")
    @Size(max = 20, message = "UserName can have 20 character most")
    private String userName;

    @Email(message = "Email not in a valid form")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "password is must")
    @Size(min = 4, message = "password length higher than 4")
    @Size(max = 10, message = "password length lower than 10")
    private String password;

    private Set<String> roles;

    public SignUpRequestDto(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = null;
    }
}
