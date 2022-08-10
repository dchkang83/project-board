package com.main.gundam.controller.api.v1;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.domain.User;
import com.main.gundam.config.auth.JwtToken;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.service.UserService;
import com.main.gundam.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
  private final UserRepository userRepository;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final AuthenticationManager authenticationManager; // @Autowired
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserService userService;

  @PostMapping("join")
  public String join(@RequestBody User user) {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      user.setRoles("ROLE_USER");

      log.info("JOIN : " + user);

      userRepository.save(user);
      return "화원가입완료";
  }

  @RequestMapping(value = "refresh", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public JwtToken.Response refresh(
      final HttpServletResponse response,
      @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
      @RequestHeader(value = "REFRESH-TOKEN") String refreshToken) {

        accessToken = accessToken.substring("Bearer ".length());
        refreshToken = refreshToken.substring("Bearer ".length());

    JwtToken.Response jwtResponse = userService.refreshToken(accessToken, refreshToken);

    jwtTokenProvider.setHeaderAccessToken(response, jwtResponse.getAccessToken());
    jwtTokenProvider.setHeaderRefreshToken(response, jwtResponse.getRefreshToken());

    return null;
  }

  // @PostMapping("signin")
  @RequestMapping(value = "signin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public JwtToken.Response signin(
      final HttpServletRequest request,
      final HttpServletResponse response,
      @RequestBody JwtToken.Request jwtRequest) {

    // User user = userService.findByIdPw(request.getUsername()).orElseThrow(() ->
    // new IllegalArgumentException("없는 사용자입니다."));
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      jwtRequest.getUsername(), jwtRequest.getPassword());

    // PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이 리턴됨
    // DB에 있는 username과 password가 일치한다.
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    // 로그인이 되었다는 뜻.
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    log.info("LOGIN SUCCESS >>> " + principalDetails.getUser().getUsername());

    String accessToken = jwtTokenProvider.generateAccessToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken();

    JwtToken.Response jwtResponse = JwtToken.Response.builder().accessToken(accessToken).build();

    jwtTokenProvider.setHeaderAccessToken(response, accessToken);
    jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

    return jwtResponse;
  }
}
