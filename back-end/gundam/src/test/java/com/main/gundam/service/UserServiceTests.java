package com.main.gundam.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.main.gundam.domain.User;
import com.main.gundam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

// @RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    
    @Test
    @DisplayName("사용자 등록")
    public void saveUserTest() {
        //given
        User user = new User();
        user.setUsername("test_user111");
        
        Long fakeUserNo = (long) 111;
        ReflectionTestUtils.setField(user, "userNo", fakeUserNo);

        // mocking
        given(userRepository.save(any()))
            .willReturn(user);
        given(userRepository.findById(fakeUserNo))
            .willReturn(Optional.ofNullable(user));

        // when
        Long newUserNo = userService.addUser(user);
        User retrivedUser = userRepository.findById(newUserNo).get();

        // then
        Assertions.assertEquals(retrivedUser.getUsername(), "test_user111");
    }
}
