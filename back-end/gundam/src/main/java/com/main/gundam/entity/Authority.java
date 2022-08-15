package com.main.gundam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
// import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_authority")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
  @Id
  @Column(name = "authority_name", length = 50)
  private String authorityName;
}
