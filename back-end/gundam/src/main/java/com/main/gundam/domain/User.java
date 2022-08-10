package com.main.gundam.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Table(name = "TEST") // user table은 이미 사용하고 있는경우가 ..많아서 선언해줌
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private long userNo;
    private String username;
    private String password;
    private String roles; // USER, ADMIN
    private String refreshToken;
    
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }
}
