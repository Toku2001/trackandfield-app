package io.github.Toku2001.trackandfieldapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.repository.UserMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegisterUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Test
    void registerUser_success() throws Exception {
        String json = """
        {
          "userName": "testUser",
          "userPassword": "password123",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.registerNumber").isNumber()); 
    }
    
    @Test
    void registerUser_SizeOver_UserName() throws Exception {
        String json = """
        {
          "userName": "sizeOfUserNameMoreThan16",
          "userPassword": "password123",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        	.andExpect(status().isBadRequest());	
    }
    
    @Test
    void registerUser_Nothing_UserName() throws Exception {
        String json = """
        {
          "userName": "",
          "userPassword": "password123",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
		        .andExpect(status().isBadRequest())
		        .andExpect(jsonPath("$.error").value("ユーザー名は必須です"));
    }
    
    @Test
    void registerUser_Nothing_UserPassword() throws Exception {
        String json = """
        {
          "userName": "sizeOfUserNameMoreThan16",
          "userPassword": "",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
		        .andExpect(status().isBadRequest())
		        .andExpect(jsonPath("$.error").value("パスワードは必須です"));
    }

    @Test
    void registerUser_fails_when_service_returns_zero() throws Exception {
        // 前提：すでに登録済みのユーザーが存在
        userMapper.registerUser("testUser", "password123", "test@example.com");

        String json = """
        {
          "userName": "testUser",
          "userPassword": "password123",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("登録できませんでした"));
    }
}