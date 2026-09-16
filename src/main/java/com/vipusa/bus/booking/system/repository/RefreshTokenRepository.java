package com.vipusa.bus.booking.system.repository;

import com.vipusa.bus.booking.system.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String token);
    Optional<RefreshToken> findByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
}