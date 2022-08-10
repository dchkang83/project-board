package com.main.gundam.service;

public class JwtService {
  // @Transactional
  // public String reissueRefreshToken(String refreshToken) throws RuntimeException {
  //   // refresh token을 디비의 그것과 비교해보기
  //   Authentication authentication = getAuthentication(refreshToken);
  //   RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(authentication.getName())
  //       .orElseThrow(() -> new UsernameNotFoundException("userId : " + authentication.getName() + " was not found"));

  //   if (findRefreshToken.getToken().equals(refreshToken)) {
  //     // 새로운거 생성
  //     String newRefreshToken = createRefreshToken(authentication);
  //     findRefreshToken.changeToken(newRefreshToken);
  //     return newRefreshToken;
  //   } else {
  //     log.info("refresh 토큰이 일치하지 않습니다. ");
  //     return null;
  //   }
  // }

  // @Transactional
  // public String issueRefreshToken(Authentication authentication) {
  //   String newRefreshToken = createRefreshToken(authentication);

  //   // 기존것이 있다면 바꿔주고, 없다면 만들어줌
  //   refreshTokenRepository.findByUserId(authentication.getName())
  //       .ifPresentOrElse(
  //           r -> {
  //             r.changeToken(newRefreshToken);
  //             log.info("issueRefreshToken method | change token ");
  //           },
  //           () -> {
  //             RefreshToken token = RefreshToken.createToken(authentication.getName(), newRefreshToken);
  //             log.info(" issueRefreshToken method | save tokenID : {}, token : {}", token.getUserId(),
  //                 token.getToken());
  //             refreshTokenRepository.save(token);
  //           });

  //   return newRefreshToken;
  // }
}
