package com.main.gundam.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "T_REFRESH_TOKEN")
@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
  @Id
  @Column(name = "USER_NO", nullable = false)
  private long userNo;

  @Column(name = "REFRESH_TOKEN", nullable = false, length = 200)
  private String refreshToken;
}