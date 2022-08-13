package com.main.gundam.config.jwt;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;
import com.main.gundam.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// 시큐리티가 filter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탐
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게됨
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("{} - successfulAuthentication -> 인증이나 권한이 필요한 주소 요청이 됨", this.getClass());

        String bearerAccessToken = request.getHeader("X-ACCESS-TOKEN");
        
        if (bearerAccessToken == null || bearerAccessToken.startsWith("Bearer") == false) {
            chain.doFilter(request, response);
            return;
        }
        
        String accessToken = jwtTokenProvider.getBearerTokenToString(bearerAccessToken);
        String username = jwtTokenProvider.getUsernameByAccessToken(accessToken);

        // 서명이 정상적으로 됨
        if (username != null) {
          userRepository.findOneWithAuthoritiesByUsername(username)
          .ifPresentOrElse(
            r -> {

              
              List<GrantedAuthority> grantedAuthorities = r.getAuthorities().stream()
                  .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                  .collect(Collectors.toList());
              // return new org.springframework.security.core.userdetails.User(user.getUsername(),
              //     user.getPassword(),
              //     grantedAuthorities);
              // return new PrincipalDetails(r, grantedAuthorities);


              PrincipalDetails principalDetails = new PrincipalDetails(r, grantedAuthorities);

              // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
              Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

              // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
              SecurityContextHolder.getContext().setAuthentication(authentication);
              
            },
            () -> {
              
            });

            chain.doFilter(request, response);


            // User userEntity = userRepository.findByUsername(username);
            // PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            // Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            // chain.doFilter(request, response);
        }
    }
}
