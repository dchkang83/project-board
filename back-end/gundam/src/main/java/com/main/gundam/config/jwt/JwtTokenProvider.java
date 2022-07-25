package com.main.gundam.config.jwt;

import java.util.Date;

import org.springframework.security.core.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.main.gundam.config.auth.PrincipalDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenProvider {
    private static final String JWT_SECRET = "secretKey";

    // 토큰 유효시간
    private static final int JWT_EXPIRATION_MS = 604800000;

    // jwt 토큰 생성
    public static String generateToken(Authentication authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        // return Jwts.builder()
        //     .setSubject((String) authentication.getPrincipal()) // 사용자
        //     .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
        //     .setExpiration(expiryDate) // 만료 시간 세팅
        //     // .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
        //     .sign(Algorithm.HMAC512("secret-cos"));
            
        //     .compact();


        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // RSA 방식은 아니고 Hash암호방식
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.))
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // (60000)1분 * 10 => 10분

                .withClaim("id", principalDetails.getUser().getUserNo())
                .withClaim("username", principalDetails.getUser().getUsername())

//                .sign(Algorithm.HMAC512(JwtProperties.))
                .sign(Algorithm.HMAC512("secret-cos"));

        // response.addHeader("Authorization", "Bearer " + jwtToken);

        return jwtToken;
    }

//     // Jwt 토큰에서 아이디 추출
//     public static String getUserIdFromJWT(String token) {
//         Claims claims = Jwts.parser()
//             .setSigningKey(JWT_SECRET)
//             .parseClaimsJws(token)
//             .getBody();

//         return claims.getSubject();
//     }

//     // Jwt 토큰 유효성 검사
//     public static boolean validateToken(String token) {
//         try {
//             Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
//             return true;
//         } catch (SignatureException ex) {
//             log.error("Invalid JWT signature");
//         } catch (MalformedJwtException ex) {
//             log.error("Invalid JWT token");
//         } catch (ExpiredJwtException ex) {
//             log.error("Expired JWT token");
//         } catch (UnsupportedJwtException ex) {
//             log.error("Unsupported JWT token");
//         } catch (IllegalArgumentException ex) {
//             log.error("JWT claims string is empty.");
//         }
//         return false;
//     }
}
