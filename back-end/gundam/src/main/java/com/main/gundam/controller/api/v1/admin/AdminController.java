package com.main.gundam.controller.api.v1.admin;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.main.gundam.config.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {
  // admin 권한만 - 접근가능
  @GetMapping("main")
  public String main(Authentication authentication) {
      PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
      log.info("authentication : " + principalDetails.getUsername());
      return "admin - main";
  }
}
