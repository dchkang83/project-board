package com.main.gundam.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.gundam.domain.Authority;
import com.main.gundam.domain.User;
import com.main.gundam.dto.UserDto;
import com.main.gundam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findByIdPw(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findOneWithAuthoritiesByUsername(String username) {
    return userRepository.findOneWithAuthoritiesByUsername(username);
  }

  public User addUser(UserDto userDto) {
    User user = User.builder()
        .username(userDto.getUsername())
        .password(passwordEncoder.encode(userDto.getPassword()))
        .roles("ROLE_USER")
        .build();

    log.info("JOIN : " + user);

    return userRepository.save(user);
  }

  /**
   * 회원가입
   * 
   * @param userDto
   * @return
   * @throws Exception
   */
  public User signup(UserDto userDto) throws Exception {
    // if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
    //     // throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
    //     throw new Exception("이미 가입되어 있는 유저입니다.");
    // }

    // 빌더 패턴의 장점
    Authority authority = Authority.builder()
            .authorityName("ROLE_USER")
            .build();

    User user = User.builder()
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))

            .roles("ROLE_USER")

            // .nickname(userDto.getNickname())
            .authorities(Collections.singleton(authority))
            // .activated(true)
            .build();

    return userRepository.save(user);
  }

}
