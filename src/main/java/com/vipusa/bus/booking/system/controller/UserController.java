package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.request.EditUserRequest;
import com.vipusa.bus.booking.system.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("All users retrieved successfully")
                .response(users)
                .build());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User retrieved successfully")
                .response(user)
                .build());
    }

    @PutMapping("/editUser/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> editUser(
            @PathVariable Long userId,
            @RequestBody @Valid EditUserRequest request) {
        User user = userService.editUser(userId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User updated successfully")
                .response(user)
                .build());
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long userId) {
        boolean result = userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User deleted successfully")
                .response(result)
                .build());
    }
}