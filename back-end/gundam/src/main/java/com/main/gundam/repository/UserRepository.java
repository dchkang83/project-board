package com.main.gundam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.gundam.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
    public User findByUserNo(Long userNo);
}
