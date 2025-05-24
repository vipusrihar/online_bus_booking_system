package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.EditUserRequest;
import com.vipusa.bus.booking.system.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    void save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> getUserById(Long userId);

    User editUser(Long userId, EditUserRequest request);

    boolean deleteUser(Long userId);
}
