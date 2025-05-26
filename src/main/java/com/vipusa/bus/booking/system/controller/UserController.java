package com.vipusa.bus.booking.system.controller;

import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.request.EditUserRequest;
import com.vipusa.bus.booking.system.response.ApiResponse;
import com.vipusa.bus.booking.system.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getAll")
    ResponseEntity<ApiResponse<List<User>>> getAllUser(){
        log.info("Getting All Users details");
        try {
           List<User> userList = userService.getAllUser();
            if(userList.isEmpty()){
                log.warn("No Users found in database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<List<User>>builder()
                                .response(null)
                                .isSuccess(false)
                                .message("No Users available")
                                .build());
            }
            log.info("Successfully retrieved {} Users", userList.size());
            return ResponseEntity.ok()
                    .body(ApiResponse.<List<User>>builder()
                            .response(userList)
                            .isSuccess(true)
                            .message("Successfully Retrieved "+ userList.size() + " Users")
                            .build());
        }catch (Exception e){
            log.error("Error fetching all users : {}" ,e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<User>>builder()
                            .response(null)
                            .isSuccess(false)
                            .message("Failed to Fetch User :"+e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getBusById(
            @PathVariable @Min(1) Long userId) {
        log.info("Fetching bus with ID: {}", userId);
        try {
            User user = userService.getUserById(userId).orElseThrow();
            log.warn("No users found in database");
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(ApiResponse.<User>builder()
                            .isSuccess(true)
                            .message("User retrieved successfully")
                            .response(user)
                            .build());
        }
        catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<User>builder()
                            .isSuccess(false)
                            .message("Failed to fetch user")
                            .response(null)
                            .build());
        }
    }

    @PutMapping("/editUser/{userId}")
    public ResponseEntity<ApiResponse<User>> editUser(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid EditUserRequest request) {

        log.info("Changing the user with the ID: {}", userId);

        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("No users found in database");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<User>builder()
                            .isSuccess(false)
                            .message("User not found with ID: " + userId)
                            .response(null)
                            .build());
        }

        try {
            User updatedUser = userService.editUser(userId, request);

            return ResponseEntity.ok()
                    .body(ApiResponse.<User>builder()
                            .isSuccess(true)
                            .message("User updated successfully")
                            .response(updatedUser)
                            .build());

        } catch (Exception e) {
            log.error("Error updating user with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<User>builder()
                            .isSuccess(false)
                            .message("Error updating user: " + e.getMessage())
                            .response(null)
                            .build());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteBus(@PathVariable @Min(1) Long userId) {
        log.info("Attempting to delete user with ID: {}", userId);
        try {
            boolean isDeleted = userService.deleteUser(userId);

            if (isDeleted) {
                log.info("Successfully deleted user with ID: {}", userId);
                return ResponseEntity.ok()
                        .body(ApiResponse.<Boolean>builder()
                                .isSuccess(true)
                                .message("User with ID " + userId + " was successfully deleted")
                                .response(true)
                                .build());
            }

            log.warn("User with ID {} could not be deleted (might not exist)", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("User with ID " + userId + " could not be found or deleted")
                            .response(false)
                            .build());

        } catch (Exception e) {
            log.error("Error deleting bus with ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Boolean>builder()
                            .isSuccess(false)
                            .message("Failed to delete user: " + e.getMessage())
                            .response(false)
                            .build());
        }
    }

}
