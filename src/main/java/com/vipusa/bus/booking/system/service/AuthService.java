package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.ApiResponse;
import com.vipusa.bus.booking.system.DTO.LoginRequestDto;
import com.vipusa.bus.booking.system.DTO.SignUpRequestDto;
import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<ApiResponse<?>> signUpUser (SignUpRequestDto signUpRequestDto)throws UserAlreadyExistsException, RoleNotFoundException;;

    ResponseEntity<ApiResponse<?>> loginUser(LoginRequestDto loginRequestDto);

}
