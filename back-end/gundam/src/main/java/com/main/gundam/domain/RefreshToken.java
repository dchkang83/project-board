package com.main.gundam.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
  @Id
  @Column(nullable = false)
  private long userNo;

  @Column(nullable = false)
  private String refreshToken;
}