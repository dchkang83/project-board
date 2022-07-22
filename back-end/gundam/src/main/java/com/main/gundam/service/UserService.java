package com.main.gundam.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Long addUser(User user) {
        return userRepository.save(user).getUserNo();
    }
}
