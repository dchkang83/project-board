package com.main.gundam.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public Long addUser(User user) {
      return userRepository.save(user).getUserNo();
  }

  public User findByIdPw(String username) {
    return userRepository.findByUsername(username);
  }

}
