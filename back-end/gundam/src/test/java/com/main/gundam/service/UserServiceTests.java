package com.main.gundam.service;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.main.gundam.domain.User;
import com.main.gundam.dto.UserDto;
import com.main.gundam.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;
  
  @Mock
  private PasswordEncoder bCryptPasswordEncoder;

  @Test
  @DisplayName("사용자 등록")
  public void saveUserTest() {
    // given
    String username = "test_user111";
    String password = "pass111";
    User user = User.builder()
        .username(username)
        .password(password)
        .build();

    UserDto userDto = UserDto.builder()
        .username(username)
        .password(password)
        .build();

    Long fakeUserNo = (long) 111;
    ReflectionTestUtils.setField(user, "userNo", fakeUserNo);

    // mocking
    // given(bCryptPasswordEncoder.encode())
    //   .willReturn();
    given(userRepository.save(any()))
        .willReturn(user);
    given(userRepository.findById(fakeUserNo))
        .willReturn(Optional.ofNullable(user));

    // when
    Long newUserNo = userService.addUser(userDto);
    User retrivedUser = userRepository.findById(newUserNo).get();

    // then
    Assertions.assertEquals(retrivedUser.getUsername(), username);
  }
}
