package com.main.gundam.controller.api.v1;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.main.gundam.dto.UserDto;
import com.main.gundam.entity.User;
import com.main.gundam.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
  private final UserService userService;

  @PostMapping("signup")
  public ResponseEntity<User> signup(
    @RequestBody UserDto userDto) throws Exception {
    // @Valid @RequestBody UserDto userDto) {
      
    // Long newUserNo = userService.addUser(userDto);

    // User user = userService.signup(userDto);
    // return "화원가입완료 : " + user.getUsername();


    return ResponseEntity.ok(userService.signup(userDto));
    // return ResponseEntity.ok(userService.addUser(userDto));
  }
}
