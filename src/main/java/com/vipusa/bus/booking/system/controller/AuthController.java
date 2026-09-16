package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.exception.UserAlreadyExistsException;
import com.vipusa.bus.booking.system.request.LoginRequestDto;
import com.vipusa.bus.booking.system.request.RefreshTokenRequest;
import com.vipusa.bus.booking.system.request.SignUpRequestDto;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.response.AuthResponse;
import com.vipusa.bus.booking.system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(
            @Valid @RequestBody SignUpRequestDto request) throws RoleNotFoundException, UserAlreadyExistsException {

        AuthResponse response = authService.signUpUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "User account has been successfully created!",
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequestDto request) {

        AuthResponse response = authService.loginUser(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully logged in",
                        response
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse response = authService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Token refreshed successfully",
                        response
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request.refreshToken());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Logged out successfully",
                        null
                )
        );
    }
}