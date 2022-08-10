package com.main.gundam.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.gundam.config.auth.JwtToken;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Long addUser(User user) {
        return userRepository.save(user).getUserNo();
    }

    public User findByIdPw(String username) {
      return userRepository.findByUsername(username);
    }

    @Transactional
    // public UserLoginResponseDto refreshToken(String token, String refreshToken) {
    public JwtToken.Response refreshToken(String token, String refreshToken) {
      // 아직 만료되지 않은 토큰으로는 refresh 할 수 없음
      if (!jwtTokenProvider.validateToken(token)) {
        throw new AccessDeniedException("AccessDeniedException 1");
      }

      // User user = userRepository.findByUsername(Long.valueOf(jwtTokenProvider.getUsernameFromJWT(token)));
      User user = userRepository.findByUsername(jwtTokenProvider.getUserEmail(token));
        // .orElseThrow(NotFoundException::new);
          // .orElseThrow(UserNotFoundException::new);

      if (!jwtTokenProvider.validateToken(user.getRefreshToken()) || !refreshToken.equals(user.getRefreshToken())) {
        throw new AccessDeniedException("AccessDeniedException 2");
      }

      // access token 과 refresh token 모두를 재발급
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
      String newRefreshToken = jwtTokenProvider.generateRefreshToken();

      user.setRefreshToken(newRefreshToken);
      userRepository.save(user);

      JwtToken.Response response = JwtToken.Response.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();

      return response;
    }    
}
