package com.main.gundam.controller.v1;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.config.auth.JwtToken;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.domain.User;
import com.main.gundam.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class V1SignTestController {
    private final AuthenticationManager authenticationManager; // @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    // @PostMapping("signin")
    @RequestMapping(
        value = "signin",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public JwtToken.Response signin(
        final HttpServletRequest req,
        final HttpServletResponse res,        
        @RequestBody JwtToken.Request request) {
    
        // User user = userService.findByIdPw(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
        User user = userService.findByIdPw(request.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이 리턴됨
        // DB에 있는 username과 password가 일치한다.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 로그인이 되었다는 뜻.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("LOGIN SUCCESS >>> " + principalDetails.getUser().getUsername());
        
        String token = jwtTokenProvider.generateToken(authentication);
        JwtToken.Response response = JwtToken.Response.builder().token(token).build();

        res.addHeader("Authorization", "Bearer " + token);

        return response;
    }
}
