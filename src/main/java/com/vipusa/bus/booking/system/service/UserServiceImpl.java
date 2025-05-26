package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.request.EditUserRequest;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.existsByName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    @Override
    public User editUser(Long userId, EditUserRequest request) {
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Update user fields from request
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Attempt to delete non-existent user with ID: {}", userId);
            return false;
        }

        userRepository.deleteById(userId);
        return true;
    }
}
