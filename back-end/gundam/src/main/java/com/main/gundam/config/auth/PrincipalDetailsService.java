package com.main.gundam.config.auth;

import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;
import com.main.gundam.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// @Service
@Component
// @RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userService.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
