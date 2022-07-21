package com.main.gundam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;

import com.main.gundam.model.User;
import com.main.gundam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

// @RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Slf4j
public class UserServiceTests {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveMemberTest() {
        //given
        User user = new User();
        user.setUsername("test_user111");
        userRepository.save(user);

        // when
        User retrivedUser = userRepository.findById(user.getId()).get();

        // then
        Assertions.assertEquals(retrivedUser.getUsername(), "test_user111");
    }
}
