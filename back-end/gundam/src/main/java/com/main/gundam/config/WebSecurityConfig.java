package com.main.gundam.config;

import com.main.gundam.config.auth.PrincipalDetailsService;
import com.main.gundam.config.jwt.JwtAuthenticationFilter;
import com.main.gundam.config.jwt.JwtAuthorizationFilter;
import com.main.gundam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PrincipalDetailsService(userRepository);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement()        
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(corsFilter)  // Controller 어노테이트 @CrossOrigin(인증 X), 시큐리티 필터에 등록 인증(O)
        .formLogin()
        .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
        .and()
        .httpBasic().disable()

        .authorizeRequests()        
        .antMatchers("/signin").permitAll()

        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll()
//                .and().formLogin().loginProcessingUrl("/login")
        ;

        http
        .addFilterBefore(jwtAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class)
        ;

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

	public JwtAuthorizationFilter jwtAuthenticationFilter(UserRepository userRepository) {
        return new JwtAuthorizationFilter(userRepository);
	}
}
