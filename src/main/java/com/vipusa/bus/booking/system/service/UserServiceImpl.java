package com.vipusa.bus.booking.system.service;

import com.vipusa.bus.booking.system.DTO.EditUserRequest;
import com.vipusa.bus.booking.system.entity.User;
import com.vipusa.bus.booking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.empty();
    }

    @Override
    public User editUser(Long userId, EditUserRequest request) {
        return null;
    }

    @Override
    public boolean deleteUser(Long userId) {
        return false;
    }
}
