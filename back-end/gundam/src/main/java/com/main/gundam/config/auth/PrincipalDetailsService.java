package com.main.gundam.config.auth;

import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
// @Component
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("{} - loadUserByUsername", this.getClass());

        // TODO. repository X, service 로 변경해보자. (아래처럼)
        // https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
        // Account account = accountService.findAccountByEmail(email);

        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
