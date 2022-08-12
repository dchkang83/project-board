package com.main.gundam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDto {
  
  private Long userNo;
  private String accessToken;
  private String refreshToken;
}
