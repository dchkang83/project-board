package com.main.gundam.service;

import java.io.ObjectInputFilter.Config;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.domain.RefreshToken;
import com.main.gundam.domain.User;
import com.main.gundam.dto.TokenDto;
import com.main.gundam.repository.RefreshTokenRepository;
import com.main.gundam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
  @Autowired
  private ApplicationContext context;
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken);
  }
  
  /**
   * save refresh token
   * 
   * @param tokenDto
   */
  public void saveRefreshToken(TokenDto tokenDto) {
    refreshTokenRepository.findByUserNo(tokenDto.getUserNo())
        .ifPresentOrElse(
            r -> {
              r.setRefreshToken(tokenDto.getRefreshToken());
            },
            () -> {
              RefreshToken token = RefreshToken.builder().userNo(tokenDto.getUserNo())
                  .refreshToken(tokenDto.getRefreshToken()).build();
              refreshTokenRepository.save(token);
            });
  }

  public TokenDto refresh(String bearerRefreshToken) {

    JwtTokenProvider jwtTokenProvider = context.getBean(JwtTokenProvider.class);

    String refreshToken = jwtTokenProvider.getBearerTokenToString(bearerRefreshToken);

    // 유효한 refresh token 인지 체크
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new AccessDeniedException("AccessDeniedException 2");
    }

    // refresh token 있으면 값 반환, 없으면 Exception
    RefreshToken findRefreshToken = this.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new UsernameNotFoundException("refresh token was not found"));

    // refresh token 을 활용하여 user email 정보 획득
    User user = userRepository.findByUserNo(findRefreshToken.getUserNo());

    // access token 과 refresh token 모두를 재발급
    Authentication authentication = jwtTokenProvider.getAuthenticationByUsername(user.getUsername());
    String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken();

    TokenDto tokenDto = TokenDto.builder().userNo(findRefreshToken.getUserNo()).accessToken(newAccessToken)
        .refreshToken(newRefreshToken).build();

    this.saveRefreshToken(tokenDto);

    return tokenDto;
  }
}
