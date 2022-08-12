package com.main.gundam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
  // @NotNull
  // @Size(min = 5, max = 30)
  private String username;

  // @NotNull
  // @Size(min = 5, max = 100)
  private String password;
}
