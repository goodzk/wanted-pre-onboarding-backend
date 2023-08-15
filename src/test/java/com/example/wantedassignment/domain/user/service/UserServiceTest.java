package com.example.wantedassignment.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import com.example.wantedassignment.domain.user.dto.request.JoinUser;
import com.example.wantedassignment.domain.user.dto.response.user.JoinResultDto;
import com.example.wantedassignment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        JoinUser joinUser = new JoinUser(
                "test@gmail.com", "12345678", "이름"
        );
        userService.join(joinUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입() {
        // given
        JoinUser joinUser = new JoinUser(
                "test11@gmail.com", "12345678", "이름"
        );

        // when
        final JoinResultDto join = userService.join(joinUser);

        // then
        assertThat(join.isSuccess()).isTrue();
    }
}