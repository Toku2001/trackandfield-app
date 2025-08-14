package io.github.Toku2001.trackandfieldapp.unittest.resetpassword;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.service.password.PasswordResetService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository") 
public class ResetPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordResetService passwordResetService;

    @Test
    void requestPasswordReset_success() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");

        // サービスをモック化して true を返す
        when(passwordResetService.requestReset(any(PasswordResetRequest.class)))
            .thenReturn(true);

        mockMvc.perform(post("/auth/request-password-reset")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sendLink").value(true))
            .andExpect(jsonPath("$.responseMessage").value("再設定リンクを送信しました。"));

        verify(passwordResetService, times(1)).requestReset(any(PasswordResetRequest.class));
    }
    
    @Test
    void requestPasswordReset_failer() throws Exception {
    	PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");
    	
    	when(passwordResetService.requestReset(any(PasswordResetRequest.class)))
        .thenReturn(false);
    	
    	mockMvc.perform(post("/auth/request-password-reset")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sendLink").value(false))
            .andExpect(jsonPath("$.responseMessage").value("再設定リンクを送信できませんでした。"));

        verify(passwordResetService, times(1)).requestReset(any(PasswordResetRequest.class));
    }
    
    @Test
    void requestPasswordReset_databaseOperationException() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");

        // サービス層が DatabaseOperationException を投げるように設定
        when(passwordResetService.requestReset(any(PasswordResetRequest.class)))
            .thenThrow(new DatabaseOperationException("DBエラー"));

        mockMvc.perform(post("/auth/request-password-reset")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$.sendLink").value(false))
            .andExpect(jsonPath("$.responseMessage").value("ユーザー名またはメースアドレスが違います。"));

        verify(passwordResetService, times(1)).requestReset(any(PasswordResetRequest.class));
    }
}
