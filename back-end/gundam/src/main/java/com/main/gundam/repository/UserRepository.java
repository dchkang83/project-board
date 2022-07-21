package com.main.gundam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.gundam.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
