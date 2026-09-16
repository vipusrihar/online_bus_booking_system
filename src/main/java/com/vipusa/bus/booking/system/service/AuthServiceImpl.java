package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.RoleFactory;
import com.vipusa.bus.booking.system.config.jwt.JwtUtils;
import com.vipusa.bus.booking.system.entity.RefreshToken;
import com.vipusa.bus.booking.system.entity.Role;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.exception.InvalidRefreshTokenException;
import com.vipusa.bus.booking.system.exception.RoleNotFoundException;
import com.vipusa.bus.booking.system.exception.UserAlreadyExistsException;
import com.vipusa.bus.booking.system.repository.RefreshTokenRepository;
import com.vipusa.bus.booking.system.repository.UserRepository;
import com.vipusa.bus.booking.system.request.LoginRequestDto;
import com.vipusa.bus.booking.system.request.SignUpRequestDto;
import com.vipusa.bus.booking.system.response.AuthResponse;
import com.vipusa.bus.booking.system.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleFactory roleFactory;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.refresh-token-expiration-ms:604800000}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public AuthResponse signUpUser(SignUpRequestDto request)
            throws UserAlreadyExistsException, RoleNotFoundException {

        validateUserDoesNotExist(request);

        User user = createUser(request);

        User savedUser = userRepository.save(user);

        Authentication authentication = authenticate(request.getEmail(), request.getPassword());

        String accessToken = jwtUtils.generateJwtToken(authentication);

        String refreshToken = createRefreshToken(savedUser);

        UserResponse userResponse = mapToUserResponse(savedUser);

        return new AuthResponse(
                accessToken,
                refreshToken,
                userResponse
        );
    }


    @Override
    @Transactional
    public AuthResponse loginUser(LoginRequestDto request) {

        Authentication authentication = authenticate( request.getEmail(), request.getPassword());

        String accessToken = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalStateException( "Authenticated user was not found"));

        String refreshToken = createRefreshToken(user);

        UserResponse userResponse = mapToUserResponse(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                userResponse
        );
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String rawRefreshToken) {

        String tokenHash = hashToken(rawRefreshToken);

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenHash(tokenHash).map(this::verifyRefreshToken)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Invalid or expired refresh token"));

        User user = refreshToken.getUser();

        refreshTokenRepository.delete(refreshToken);

        String newAccessToken = jwtUtils.generateTokenFromEmail(user.getEmail());

        String newRefreshToken = createRefreshToken(user);

        UserResponse userResponse = mapToUserResponse(user);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                userResponse
        );
    }


    @Override
    @Transactional
    public void logout(String rawRefreshToken) {

        String tokenHash = hashToken(rawRefreshToken);

        refreshTokenRepository .findByTokenHash(tokenHash)
                .ifPresent(refreshTokenRepository::delete);
    }


    private Authentication authenticate(String email,String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }


    private void validateUserDoesNotExist( SignUpRequestDto request) throws UserAlreadyExistsException {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Provided email already exists");
        }

        if (userRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("Provided username already exists");
        }
    }


    private User createUser(SignUpRequestDto request ) throws RoleNotFoundException {

        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(determineRoles(request.getRoles()))
                .build();
    }

    private Set<Role> determineRoles(Set<String> requestedRoles) throws RoleNotFoundException {

        Set<Role> roles = new HashSet<>();
        if (requestedRoles == null || requestedRoles.isEmpty()) {
            roles.add(roleFactory.getInstance("USER"));
            return roles;
        }

        for (String role : requestedRoles) {
            roles.add(roleFactory.getInstance(role));
        }
        return roles;
    }


    private String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser_Id(user.getId());
        String rawToken = generateSecureToken();
        String tokenHash = hashToken(rawToken);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private String generateSecureToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is not available", exception);
        }
    }

    private RefreshToken verifyRefreshToken( RefreshToken token ) {

        if (token.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        return token;
    }

    private UserResponse mapToUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}