package com.main.gundam.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탐
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게됨
   */
  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    log.info("{} - successfulAuthentication -> 인증이나 권한이 필요한 주소 요청이 됨", this.getClass());

    /*
     * String bearerAccessToken = httpServletRequest.getHeader("X-ACCESS-TOKEN");
     * 
     * if (bearerAccessToken == null || bearerAccessToken.startsWith("Bearer") ==
     * false) {
     * filterChain.doFilter(httpServletRequest, httpServletResponse);
     * return;
     * }
     * 
     * String accessToken =
     * jwtTokenProvider.getBearerTokenToString(bearerAccessToken);
     * String username = jwtTokenProvider.getUsernameByAccessToken(accessToken);
     * 
     * // 서명이 정상적으로 됨
     * if (username != null) {
     * userRepository.findOneWithAuthoritiesByUsername(username)
     * .ifPresentOrElse(
     * r -> {
     * List<GrantedAuthority> grantedAuthorities = r.getAuthorities().stream()
     * .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
     * .collect(Collectors.toList());
     * 
     * PrincipalDetails principalDetails = new PrincipalDetails(r,
     * grantedAuthorities);
     * 
     * // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
     * Authentication authentication = new
     * UsernamePasswordAuthenticationToken(principalDetails, null,
     * principalDetails.getAuthorities());
     * // Authentication authentication =
     * jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
     * 
     * // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
     * SecurityContextHolder.getContext().setAuthentication(authentication);
     * 
     * },
     * () -> {
     * 
     * });
     * 
     * filterChain.doFilter(httpServletRequest, httpServletResponse);
     * }
     */
    // DB까지 체크 할 필요성...?? 암튼 아래와 같이 변경
    String bearerAccessToken = httpServletRequest.getHeader("X-ACCESS-TOKEN");
    String accessToken = jwtTokenProvider.getBearerTokenToString(bearerAccessToken);

    if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
      Authentication authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication); // resolveToke을 통해 토큰을 받아와서 유효성 검증을 하고 정상 토큰이면 SecurityContext에 저장
      log.debug("Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
    } else {
      log.debug("유효한 JWT 토큰이 없습니다");
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse); // 다음 Filter를 실행하기 위한 코드. 마지막 필터라면 필터 실행 후 리소스를 반환한다.
  }
}
