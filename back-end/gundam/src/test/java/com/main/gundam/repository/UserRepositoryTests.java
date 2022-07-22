package com.main.gundam.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.main.gundam.domain.User;
import lombok.extern.slf4j.Slf4j;

// @RunWith(SpringRunner.class)
@SpringBootTest // 전체 빈을 다 등록하고, 모든 application.yml를 반영해서 테스트를 하기 때문에 통합테스트용으로 많이 사용
@Rollback(true)
@Slf4j
public class UserRepositoryTests {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        //given
        User user = new User();
        user.setUsername("test_user111");

        // when
        userRepository.save(user);

        User retrivedUser = userRepository.findById(user.getUserNo()).get();

        // then
        Assertions.assertEquals(retrivedUser.getUsername(), "test_user111");
    }
}
