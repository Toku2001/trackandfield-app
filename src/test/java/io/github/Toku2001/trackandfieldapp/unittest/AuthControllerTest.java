package io.github.Toku2001.trackandfieldapp.unittest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.jayway.jsonpath.JsonPath;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.entity.PasswordResetToken;
import io.github.Toku2001.trackandfieldapp.repository.PasswordMapper;
import io.github.Toku2001.trackandfieldapp.repository.UserMapper;
import io.github.Toku2001.trackandfieldapp.service.password.PasswordResetService;
import io.github.Toku2001.trackandfieldapp.service.user.MailService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
public class AuthControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private PasswordMapper passwordMapper;

    @MockBean
    private MailService mailService;

    @Autowired
    private PasswordResetService passwordResetService;

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
        				.andDo(print()) 
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
    void registerUser_SizeOver_UserPassword() throws Exception {
        String json = """
        {
          "userName": "testUser",
          "userPassword": "sizeOfUserPasswordMoreThan16",
          "userMail": "test@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        	.andExpect(status().isBadRequest());	
    }

    @Test
    void registerUser_Nothing_Annotation_Symbol_userMail() throws Exception {
        String json = """
        {
          "userName": "testUser",
          "userPassword": "password123",
          "userMail": "testexample.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
		        .andExpect(status().isBadRequest())
		        .andExpect(jsonPath("$.error").value("正しいメールアドレスの形式で入力してください"));
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
          "userName": "testUser",
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
    void registerUser_Nothing_UserMail() throws Exception {
        String json = """
        {
          "userName": "testUser",
          "userPassword": "password123",
          "userMail": ""
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
		        .andExpect(status().isBadRequest())
		        .andExpect(jsonPath("$.error").value("メールアドレスは必須です"));
    }

    @Test
    void registerUser_fails_when_service_returns_zero() throws Exception {
        
    	// 1. 事前にユーザーを登録
    	String registerJson = """
    	        {
    			    "userName": "errorUser",
    			    "userPassword": "errorPassword",
    			    "userMail": "errortest@example.com"
    	        }
    	        """;

    	        mockMvc.perform(post("/auth/register-user")
    	                        .contentType(MediaType.APPLICATION_JSON)
    	                        .content(registerJson))
    	        				.andDo(print()) 
    	                .andExpect(status().isOk())
    	                .andExpect(jsonPath("$.userName").value("errorUser"))
    	                .andExpect(jsonPath("$.registerNumber").isNumber()); 

        String json = """
        {
          "userName": "errorUser",
          "userPassword": "errorPassword",
          "userMail": "errortest@example.com"
        }
        """;

        mockMvc.perform(post("/auth/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        	.andExpect(status().isInternalServerError()) 
          .andExpect(jsonPath("$.error").value("登録できませんでした"));
    }

    @Test
    void login_success() throws Exception {
    	// 1. 事前にユーザーを登録
    	String registerJson = """
    	        {
    			    "userName": "loginuser",
    			    "userPassword": "loginpassword",
    			    "userMail": "login@example.com"
    	        }
    	        """;

    	        mockMvc.perform(post("/auth/register-user")
    	                        .contentType(MediaType.APPLICATION_JSON)
    	                        .content(registerJson))
    	        				.andDo(print()) 
    	                .andExpect(status().isOk())
    	                .andExpect(jsonPath("$.userName").value("loginuser"))
    	                .andExpect(jsonPath("$.registerNumber").isNumber()); 

        String json = """
        {
          "userName": "loginuser",
          "userPassword": "loginpassword"
        }
        """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        	.andExpect(status().isOk())
          .andExpect(jsonPath("$.userName").value("loginuser"))
          .andExpect(jsonPath("$.accessToken").isNotEmpty()); 
    }

    @Test
    void login_failed() throws Exception {
        String json = """
            {
              "userName": "failduser",
              "userPassword": "faildpassword"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isUnauthorized())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andExpect(result ->
                assertEquals("入力されたユーザー名またはパスワードが異なります。",
                             ((ResponseStatusException) result.getResolvedException()).getReason()));
    }

    @Test
    void logout_success() throws Exception {
        // ユーザー登録（ハッシュ化済み）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("logoutpassword");
        userMapper.registerUser("logoutuser", encodedPassword, "logout@example.com");

        // ログインしてトークン取得
        MockHttpServletResponse loginResponse = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "userName": "logoutuser",
                  "userPassword": "logoutpassword"
                }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn()
                .getResponse();

        String json = loginResponse.getContentAsString();
        String accessToken = JsonPath.read(json, "$.accessToken");

        // ログアウト実行
        mockMvc.perform(post("/auth/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    
    @Test
    void requestPasswordReset_success() throws Exception {
    	// ユーザー登録（ハッシュ化済み）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("resetpassword");
        userMapper.registerUser("resetuser", encodedPassword, "reset@example.com");
        String resetRequestJson = """
            {
                "userName": "resetuser",
                "userMail": "reset@example.com"
            }
            """;

        // モック: passwordMapper.insert はvoidなので doNothing
        doNothing().when(passwordMapper).insert(any(PasswordResetToken.class));

        // モック: mailService.sendPasswordResetMail もvoidなので doNothing
        doNothing().when(mailService).sendPasswordResetMail(any(PasswordResetRequest.class), anyString());

        mockMvc.perform(post("/auth/request-password-reset") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(resetRequestJson))
            .andExpect(status().isOk())
            .andExpect(content().string("再設定リンクを送信しました。"));

        // 呼び出し検証（任意）
        verify(passwordMapper, times(1)).insert(any(PasswordResetToken.class));
        verify(mailService, times(1)).sendPasswordResetMail(any(PasswordResetRequest.class), anyString());
    }
    
    @Test
    void requestPasswordReset_failure() throws Exception {
        String resetRequestJson = """
            {
                "userName": "resetfailuser",
                "userMail": "resetfail@example.com"
            }
            """;

        // モック: passwordMapper.insert はvoidなので doNothing
        doNothing().when(passwordMapper).insert(any(PasswordResetToken.class));

        // モック: mailService.sendPasswordResetMail もvoidなので doNothing
        doNothing().when(mailService).sendPasswordResetMail(any(PasswordResetRequest.class), anyString());

        mockMvc.perform(post("/auth/request-password-reset") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(resetRequestJson))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void resetPassword_success() throws Exception {
        String requestJson = """
            {
                "token": "valid-token",
                "userName": "resetuser",
                "newUserPassword": "newStrongPassword123"
            }
            """;

        PasswordResetToken validToken = new PasswordResetToken();
        validToken.setUser_Mail("reset@example.com");
        validToken.setExpiry_Date(LocalDateTime.now().plusMinutes(10)); // 有効な期限

        // モックの設定
        when(passwordMapper.findUserMail("resetuser", "valid-token"))
            .thenReturn(Optional.of(validToken));

        when(passwordMapper.updateNewPassword(eq("resetuser"), eq("reset@example.com"), anyString()))
            .thenReturn(1); // パスワード更新成功

        doNothing().when(passwordMapper).deleteByToken("valid-token");

        // 実行と検証
        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().string("パスワードが更新されました。"));

        // 呼び出し検証
        verify(passwordMapper).updateNewPassword(eq("resetuser"), eq("reset@example.com"), anyString());
        verify(passwordMapper).deleteByToken("valid-token");
    }
    
    @Test
    void resetPassword_failure() throws Exception {
        String requestJson = """
            {
                "token": "invalid-or-expired-token",
                "userName": "resetuser",
                "newUserPassword": "newPassword"
            }
            """;

        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setUser_Mail("reset@example.com");
        expiredToken.setExpiry_Date(LocalDateTime.now().minusMinutes(1)); // トークン期限切れ

        when(passwordMapper.findUserMail("resetuser", "invalid-or-expired-token"))
            .thenReturn(Optional.of(expiredToken));

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().string("トークンが無効または期限切れです。"));
    }
}