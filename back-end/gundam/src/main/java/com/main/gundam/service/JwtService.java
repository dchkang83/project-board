package com.main.gundam.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.gundam.domain.RefreshToken;
import com.main.gundam.dto.JwtTokenDto;
import com.main.gundam.repository.RefreshTokenRepository;
import com.main.gundam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
// @RequiredArgsConstructor
@Transactional
public class JwtService {
  // private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public Optional<RefreshToken> findByRefreshToken(String refreshToken) {    
    return refreshTokenRepository.findByRefreshToken(refreshToken);
  }
  
  /**
   * 최초 발급?
   * 
   * @param jwtTokenDto
   */
  public void saveRefreshToken(JwtTokenDto jwtTokenDto) {
    refreshTokenRepository.findByUserNo(jwtTokenDto.getUserNo())
        .ifPresentOrElse(
            r -> {
              r.setRefreshToken(jwtTokenDto.getRefreshToken());
            },
            () -> {
              RefreshToken token = RefreshToken.builder().userNo(jwtTokenDto.getUserNo())
                  .refreshToken(jwtTokenDto.getRefreshToken()).build();
              refreshTokenRepository.save(token);
            });
  }
}
