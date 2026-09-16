package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.request.EditUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> getUserById(Long userId);
    List<User> getAllUser();
    User editUser(Long userId, EditUserRequest request);
    boolean deleteUser(Long userId);
}