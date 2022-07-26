package com.main.gundam.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탐
// public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
@Slf4j
// @RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    // public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
    //     super(authenticationManager);
    //     this.userRepository = userRepository;
    // }
    public JwtAuthorizationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);

        log.info("인증이나 권한이 필요한 주소 요청이 됨");


        String jwt = getJwtFromRequest(request); //request에서 jwt 토큰을 꺼낸다.
        log.info("jwt : " + jwt);

        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader : " + jwtHeader);

        // header가 잇는지 확인
        if (jwtHeader == null || jwtHeader.startsWith("Bearer") == false) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        String username = JWT.require(Algorithm.HMAC512("secret-cos")).build().verify(jwtToken).getClaim("username").asString();

        log.info("username : " + username);

        // 서명이 정상적으로 됨
        if (username != null) {
            User userEntity = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // username이 있다는건 인증이 됐다는것이기에, password 인자 없어도 인증되었다고 봐도됨
            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
