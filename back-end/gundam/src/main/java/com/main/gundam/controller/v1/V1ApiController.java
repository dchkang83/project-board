package com.main.gundam.controller.v1;

import com.main.gundam.config.auth.PrincipalDetails;
import com.main.gundam.config.auth.Token;
import com.main.gundam.config.jwt.JwtTokenProvider;
import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class V1ApiController {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager; // @Autowired
    
    @Autowired
    private UserService userService;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    // @PostMapping("signin")
    @RequestMapping(
        value = "signin",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Token.Response signin(
        // Authentication authentication,
        // @AuthenticationPrincipal PrincipalDetails userDetails,
        final HttpServletRequest req,
        final HttpServletResponse res,        
        @RequestBody Token.Request request) {
        // @RequestBody User user) {

        log.info("signin");


        // PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        log.info(request.getUsername());
        log.info(request.getPassword());
 
    
        // User user = userService.findByIdPw(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
        User user = userService.findByIdPw(request.getUsername());


        log.info("11");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        log.info("22");

        log.info(authenticationToken.toString());

        // PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이 리턴됨
        // DB에 있는 username과 password가 일치한다.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("33");              

        // 로그인이 되었다는 뜻.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("44");
        log.info("LOGIN SUCCESS >>> " + principalDetails.getUser().getUsername()); // 로그인 정상적으로 되었다는 뜻


        
        String token = JwtTokenProvider.generateToken(authentication);

        Token.Response response = Token.Response.builder().token(token).build();


//         // RSA 방식은 아니고 Hash암호방식
//         String jwtToken = JWT.create()
//                 .withSubject(principalDetails.getUsername())
// //                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.))
//                 .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // (60000)1분 * 10 => 10분

//                 .withClaim("id", principalDetails.getUser().getUserNo())
//                 .withClaim("username", principalDetails.getUser().getUsername())

// //                .sign(Algorithm.HMAC512(JwtProperties.))
//                 .sign(Algorithm.HMAC512("secret-cos"));

// //        super.successfulAuthentication(request, response, chain, authResult);
//         response.addHeader("Authorization", "Bearer " + jwtToken);


        res.addHeader("Authorization", "Bearer " + token);

        return response;
    }



    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        log.info("JOIN : " + user);
        userRepository.save(user);
        return "화원가입완료";
    }

    // user, manager, admin 권한만 - 접근가능
    @GetMapping("/api/v1/user")
    public String user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication : " + principalDetails.getUsername());
        return "user";
    }

    // manager, admin 권한만 - 접근가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin 권한만 - 접근가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
