package com.main.gundam.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Table(name = "T_USER") // user table은 이미 사용하고 있는경우가 ..많아서 선언해줌
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @JsonIgnore // JSON으로 표현해줄때 제외한다
  @Id
  @Column(name = "USER_NO")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @GeneratedValue(strategy = GenerationType.AUTO)
  private long userNo;

  @Column(name = "USER_NAME", length = 50, unique = true)
  private String username;

  @Column(name = "PASSWORD", nullable = false, length = 200)
  private String password;

  @Column(name = "ROLES", length = 50)
  private String roles; // ROLE_USER < ROLE_MANAGER < ROLE_ADMIN

  public List<String> getRoleList() {
    if (this.roles.length() > 0) {
      return Arrays.asList(this.roles.split(","));
    }

    return new ArrayList<>();
  }
}
