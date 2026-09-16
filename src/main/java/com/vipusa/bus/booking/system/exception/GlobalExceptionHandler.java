package com.vipusa.bus.booking.system.exception;

import com.vipusa.bus.booking.system.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> MethodArgumentValidExceptionHandler
            (MethodArgumentNotValidException exception ){

        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });

        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponse
                                .builder()
                                .success(false)
                                .message("Validation failed")
                                .response(errorMessage)
                                .build()
                );
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> UserAlreadyExceptionHandler(UserAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse
                                .builder()
                                .success(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> RoleNotFoundExceptionHandler(RoleNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {BookingNotFoundException.class, TripNotFoundException.class, 
            UserNotFoundException.class, BusNotFoundException.class, DepotNotFoundException.class,
            RouteNotFoundException.class, RouteStopNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> NotFoundExceptionHandler(RuntimeException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {SeatNotAvailableException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponse<?>> BadRequestExceptionHandler(RuntimeException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> AccessDeniedExceptionHandler(AccessDeniedException exception){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Access denied: " + exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> BadCredentialsExceptionHandler(BadCredentialsException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message("Invalid credentials")
                                .build()
                );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> GenericExceptionHandler(Exception exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message("An unexpected error occurred")
                                .build()
                );
    }


    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRefreshToken(
            InvalidRefreshTokenException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.failure(
                                exception.getMessage()
                        )
                );
    }



}
