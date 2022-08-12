package com.main.gundam.controller.api.v1;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.main.gundam.dto.TokenDto;
import com.main.gundam.dto.UserDto;
import com.main.gundam.dto.LoginDto;
import com.main.gundam.config.auth.JwtToken;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.service.JwtService;
import com.main.gundam.service.UserService;
import com.main.gundam.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager; // @Autowired
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("join")
  public String join(@RequestBody UserDto userDto) {
    Long newUserNo = userService.addUser(userDto);

    return "화원가입완료";
  }

  @RequestMapping(value = "refresh", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public TokenDto refresh(
      final HttpServletRequest request,
      final HttpServletResponse response,
      @RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken) {

    refreshToken = jwtTokenProvider.getBearerTokenToString(refreshToken);

    TokenDto tokenDto = jwtTokenProvider.refreshToken(refreshToken);

    jwtTokenProvider.setHeaderAccessToken(response, tokenDto.getAccessToken());
    jwtTokenProvider.setHeaderRefreshToken(response, tokenDto.getRefreshToken());

    return tokenDto;
  }

  // @PostMapping("signin")
  @RequestMapping(value = "signin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public TokenDto signin(
      final HttpServletRequest request,
      final HttpServletResponse response,
      @RequestBody LoginDto loginDto) {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDto.getUsername(), loginDto.getPassword());

    // PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이
    // 리턴됨
    // DB에 있는 username과 password가 일치한다.
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    // 로그인이 되었다는 뜻.
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    log.info("LOGIN SUCCESS >>> " + principalDetails.getUser().getUsername());

    String accessToken = jwtTokenProvider.generateAccessToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken();

    TokenDto jwtDto = TokenDto.builder().accessToken(accessToken).build();

    jwtTokenProvider.setHeaderAccessToken(response, accessToken);
    jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

    return jwtDto;
  }
}
