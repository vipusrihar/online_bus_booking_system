package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.exception.UserAlreadyExistsException;
import com.vipusa.bus.booking.system.request.LoginRequestDto;
import com.vipusa.bus.booking.system.request.SignUpRequestDto;
import com.vipusa.bus.booking.system.response.AuthResponse;

public interface AuthService {

    AuthResponse signUpUser(SignUpRequestDto request)
            throws UserAlreadyExistsException, RoleNotFoundException;

    AuthResponse loginUser(LoginRequestDto request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);
}