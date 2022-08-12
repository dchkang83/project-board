package com.main.gundam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.gundam.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  
  public Optional<RefreshToken> findByUserNo(Long userNo);
  public Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
