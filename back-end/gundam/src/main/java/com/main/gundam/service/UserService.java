package com.main.gundam.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final PasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Long addUser(UserDto userDto) {
    User user = User.builder()
        .username(userDto.getUsername())
        .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
        .roles("ROLE_USER")
        .build();

    log.info("JOIN : " + user);

    return userRepository.save(user).getUserNo();
  }

  public User findByIdPw(String username) {
    return userRepository.findByUsername(username);
  }

}
