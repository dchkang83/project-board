package com.main.gundam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  
  // @NotNull
  // @Size(min = 3, max = 50)
  private String username;

  // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  // @NotNull
  // @Size(min = 3, max = 100)
  private String password;

  // @NotNull
  // @Size(min = 3, max = 50)
  // private String nickname;
}
