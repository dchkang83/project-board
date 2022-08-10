package com.main.gundam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.main.gundam.config.auth.PrincipalDetailsService;
import com.main.gundam.config.jwt.JwtAuthenticationFilter;
import com.main.gundam.config.jwt.JwtAuthorizationFilter;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserRepository userRepository;

    private final AuthenticationConfiguration authenticationConfiguration;

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception 
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {        
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() throws Exception {
        return new JwtTokenProvider(secret, userDetailsService());
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors() // cors
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                // .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .and()
                .httpBasic().disable()

                .authorizeRequests()
                // .antMatchers("/refresh").permitAll() // 컨트롤러에서 refresh token 발행..
                // .antMatchers("/signin").permitAll() // 컨트롤러에서 인증 하는 부분 테스트

                .antMatchers("/api/v1/customer/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

        // login 주소가 호출되면 인증 및 토큰 발행 필터 추가
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider(), userRepository), UsernamePasswordAuthenticationFilter.class);

        // jwt 토큰 검사
        http.addFilterBefore(new JwtAuthorizationFilter(userRepository, jwtTokenProvider()), UsernamePasswordAuthenticationFilter.class);

        // TODO. 왜 추가 했는지 잘 기억이가 안남. 확인이 필요함
        // http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PrincipalDetailsService(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
