package com.example.wantedassignment.domain.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.wantedassignment.common.config.SecurityConfig;
import com.example.wantedassignment.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void 이메일_유효성_검사() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"testgmail.com\", \"password\":  \"12345678\", \"name\":  \"myname\"}");

        // when
        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertThat(result.getResponse().getErrorMessage())
                .isEqualTo("Forbidden");
    }

    @Test
    void 비밀번호_유효성_검사() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@gmail.com\", \"password\":  \"1234567\", \"name\":  \"myname\"}");

        // when
        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        assertThat(result.getResponse().getErrorMessage())
                .isEqualTo("Forbidden");
    }
}