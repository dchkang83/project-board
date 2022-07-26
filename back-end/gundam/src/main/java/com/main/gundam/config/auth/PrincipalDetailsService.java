package com.main.gundam.config.auth;

import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login => 여기서 동작을 안한다.
// TODO.
@Service
// @Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername IN");

        // TODO. repository X, service 로 변경해보자. (아래처럼)
        // https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
        // Account account = accountService.findAccountByEmail(email);

        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
