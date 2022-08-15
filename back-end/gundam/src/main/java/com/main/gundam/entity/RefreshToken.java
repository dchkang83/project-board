package com.main.gundam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "t_refresh_token")
@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
  @Id
  @Column(name = "user_no", nullable = false)
  private long userNo;

  @Column(name = "refresh_token", nullable = false, length = 200)
  private String refreshToken;
}