package com.main.gundam.controller.v1;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class V1ApiController {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");

        log.info("JOIN : " + user);

        userRepository.save(user);
        return "화원가입완료";
    }

    // user, manager, admin 권한만 - 접근가능
    @GetMapping("/api/v1/user")
    public String user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        log.info("authentication : " + principalDetails.getUsername());

        return "user";
    }

    // manager, admin 권한만 - 접근가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin 권한만 - 접근가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
