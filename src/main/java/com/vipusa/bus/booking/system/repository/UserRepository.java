package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    Boolean existsByName(String username);

    Boolean existsByEmail(String email);
}
