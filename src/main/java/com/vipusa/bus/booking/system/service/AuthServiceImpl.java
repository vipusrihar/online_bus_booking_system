package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.ApiResponse;
import com.vipusa.bus.booking.system.DTO.LoginRequestDto;
import com.vipusa.bus.booking.system.DTO.SignUpRequestDto;
import com.vipusa.bus.booking.system.RoleFactory;
import com.vipusa.bus.booking.system.config.jwt.JwtUtils;
import com.vipusa.bus.booking.system.entity.Role;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<ApiResponse<?>> signUpUser(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        if (userService.existsByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided email already exists. Try sign in or provide another email.");
        }
        if (userService.existsByUserName(signUpRequestDto.getUserName())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided username already exists. Try sign in or provide another username.");
        }

        User user = createUser(signUpRequestDto);
        userService.save(user);

        // Authenticate the user to generate token
        /*
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequestDto.getEmail(),
                        signUpRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        */

        String token = jwtUtils.generateTokenFromEmail(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .isSuccess(true)
                        .message("User account has been successfully created!")
                        .response(token) // Include the JWT token in response
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> loginUser(LoginRequestDto loginRequestDto) {
        User user = userService.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email Not Registered"));

        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateTokenFromEmail(loginRequestDto.getEmail());
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .isSuccess(true)
                            .message("Successfully Logged in")
                            .response(token)
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.builder()
                        .isSuccess(false)
                        .message("Invalid credentials")
                        .response(null)
                        .build()
        );
    }
    private User createUser(SignUpRequestDto signUpRequestDto) throws RoleNotFoundException {
        return User.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getUserName())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .enable(true)
                .roles(determineRoles(signUpRequestDto.getRoles()))
                .build();
    }

    private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            roles.add(roleFactory.getInstance("USER"));
        } else {
            for (String role : strRoles) {
                roles.add(roleFactory.getInstance(role));
            }
        }
        return roles;
    }
}