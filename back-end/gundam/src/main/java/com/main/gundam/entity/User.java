package com.main.gundam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Table(name = "t_user") // user table은 이미 사용하고 있는경우가 ..많아서 선언해줌
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @JsonIgnore // JSON으로 표현해줄때 제외한다
  @Id
  @Column(name = "user_no")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @GeneratedValue(strategy = GenerationType.AUTO)
  private long userNo;

  @Column(name = "user_name", length = 50, unique = true)
  private String username;

  @JsonIgnore
  @Column(name = "password", nullable = false, length = 200)
  private String password;

  // @Column(name = "roles", length = 50)
  // private String roles; // ROLE_USER < ROLE_MANAGER < ROLE_ADMIN

  // public List<String> getRoleList() {
  //   if (this.roles.length() > 0) {
  //     return Arrays.asList(this.roles.split(","));
  //   }

  //   return new ArrayList<>();
  // }

  @ManyToMany
  @JoinTable(
     name = "t_user_authority",
     joinColumns = {@JoinColumn(name = "user_no", referencedColumnName = "user_no")},
     inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
  private Set<Authority> authorities;
}
