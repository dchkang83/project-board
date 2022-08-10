package com.main.gundam.controller.api.v1.customer;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.gundam.config.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer/")
public class CustomerController {
  // customer, manager, admin 권한만 - 접근가능
  @GetMapping("main")
  public String main(Authentication authentication) {
      PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
      log.info("authentication : " + principalDetails.getUsername());
      return "customer - main";
  }
}
