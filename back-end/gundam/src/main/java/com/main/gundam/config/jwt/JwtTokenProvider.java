package com.main.gundam.config.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.domain.RefreshToken;
import com.main.gundam.domain.User;
import com.main.gundam.dto.TokenDto;
import com.main.gundam.repository.UserRepository;
import com.main.gundam.service.JwtService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenProvider {
    // 토큰 유효시간
    // private final int JWT_EXPIRATION_MS = 604800000;
    private final int JWT_EXPIRATION_MS = 60000 * 1; // 만료 시간 세팅 : 60000 (1분) * 10 => 10분
    private final int JWT_REFRESH_EXPIRATION_MS = 60000 * 10; // 만료 시간 세팅 : 60000 (1분) * 10 => 10분
    private final Key key;

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    

    public JwtTokenProvider(String secret, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        this.key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * jwt 토큰 생성
     * 
     * @param authentication
     * @return
     */
    public String generateAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
        // Date expiryDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS);
        // // 만료 시간 세팅 : 60000 (1분) * 10 => 10분

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        /*
         * iss: 토큰 발급자 (issuer)
         * sub: 토큰 제목 (subject)
         * aud: 토큰 대상자 (audience)
         * exp: 토큰의 만료시간 (expiraton), 시간은 NumericDate 형식으로 되어있어야 하며 (예: 1480849147370)
         * 언제나 현재 시간보다 이후로 설정되어있어야합니다.
         * nbf: Not Before 를 의미하며, 토큰의 활성 날짜와 비슷한 개념입니다. 여기에도 NumericDate 형식으로 날짜를 지정하며,
         * 이 날짜가 지나기 전까지는 토큰이 처리되지 않습니다.
         * iat: 토큰이 발급된 시간 (issued at), 이 값을 사용하여 토큰의 age 가 얼마나 되었는지 판단 할 수 있습니다.
         * jti: JWT의 고유 식별자로서, 주로 중복적인 처리를 방지하기 위하여 사용됩니다. 일회용 토큰에 사용하면 유용합니다. (id)
         */
        Claims claims = Jwts.claims().setSubject(String.valueOf(principalDetails.getUser().getUserNo())); // JWT payload 에 저장되는 정보단위

        // 표준 클레임 셋팅
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setId(String.valueOf(principalDetails.getUser().getUserNo())) // jti: JWT의 고유 식별자로서, 주로 중복적인 처리를 방지하기 위하여 사용됩니다. 일회용 토큰에 사용하면 유용합니다.
                .setIssuedAt(now) // iat: 토큰이 발급된 시간 (issued at), 이 값을 사용하여 토큰의 age 가 얼마나 되었는지 판단 할 수 있습니다. - 현재시간 기반으로 생성
                .setSubject(principalDetails.getUser().getUsername()) // sub: 토큰 제목 (subject) - 사용자
                .setIssuer("gundam.com") // iss: 토큰 발급자 (issuer)
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .setExpiration(expiryDate)  // exp: 토큰의 만료시간 (expiraton), 시간은 NumericDate 형식으로 되어있어야 하며 (예: 1480849147370), 언제나 현재 시간보다 이후로 설정되어있어야합니다.
        ;

        return builder.compact();
    }
    
    // jwt refresh 토큰 생성
    public String generateRefreshToken() {
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + JWT_REFRESH_EXPIRATION_MS);
      
      // 60000 * 1; // 만료 시간 세팅 : 60000 (1분) * 10 => 10분
      return Jwts.builder()
              .setIssuedAt(now)
              .setExpiration(expiryDate)
              .signWith(key, SignatureAlgorithm.HS512)
              .compact();
    }

    /**
     * Jwt 토큰에서 유져이름 추출
     * 
     * @param accessToken
     * @return
     */
    public String getUserEmail(String accessToken) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken);               

        return claims.getBody().getSubject();
    }

    // Jwt 토큰 유효성 검사
    public boolean validateToken(String accessToken) {
      try {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        return !claims.getBody().getExpiration().before(new Date());
      } catch (SignatureException ex) {
        log.error("Invalid JWT signature");
      } catch (MalformedJwtException ex) {
        log.error("Invalid JWT token");
      } catch (ExpiredJwtException ex) {
        log.error("Expired JWT token");
      } catch (UnsupportedJwtException ex) {
        log.error("Unsupported JWT token");
      } catch (IllegalArgumentException ex) {
        log.error("JWT claims string is empty.");
      }
      return false;
    }

    // bearer 빼도 순수 토큰만으로 변환
    public String getBearerTokenToString(String bearerToken) {

      return bearerToken.substring("Bearer ".length());
    }

    // 엑세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
      response.setHeader("authorization", "Bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
      response.setHeader("refreshtoken", "Bearer "+ refreshToken);
    }

    // get authentication by access token
    public Authentication getAuthenticationByAccessToken(String token) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // get authentication by access token
    public Authentication getAuthenticationByUserEmail(String userEmail) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

  public TokenDto setRefreshToken(Authentication authentication) {

    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

    String accessToken = this.generateAccessToken(authentication);
    String refreshToken = this.generateRefreshToken();
    Long userNo = principalDetails.getUser().getUserNo();

    log.info("accessToken : {}", accessToken);
    log.info("refreshToken : {}", refreshToken);

    TokenDto tokenDto = TokenDto.builder().userNo(userNo).accessToken(accessToken).refreshToken(refreshToken).build();


    jwtService.saveRefreshToken(tokenDto);


    TokenDto response = TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    return response;
  }

  public TokenDto refreshToken(String refreshToken) {
    // 유효한 refresh token 인지 체크
    if (!this.validateToken(refreshToken)) {
      throw new AccessDeniedException("AccessDeniedException 2");
    }
    
    // refresh token 있으면 값 반환, 없으면 Exception
    RefreshToken findRefreshToken = jwtService.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new UsernameNotFoundException("refresh token was not found"));

    // refresh token 을 활용하여 user email 정보 획득
    User user = userRepository.findByUserNo(findRefreshToken.getUserNo());

    // access token 과 refresh token 모두를 재발급
    Authentication authentication = this.getAuthenticationByUserEmail(user.getUsername());
    String newAccessToken = this.generateAccessToken(authentication);
    String newRefreshToken = this.generateRefreshToken();

    TokenDto tokenDto = TokenDto.builder().userNo(findRefreshToken.getUserNo()).accessToken(newAccessToken).refreshToken(newRefreshToken).build();


    // TODO. 해야함.
    // findRefreshToken.setRefreshToken("ㅁㅁㅁ");
    // refreshTokenRepository.save(findRefreshToken);

    jwtService.saveRefreshToken(tokenDto);


    return tokenDto;
  }
}
