package io.github.Toku2001.trackandfieldapp.unittest.resetpassword;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.entity.PasswordResetToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.PasswordMapper;
import io.github.Toku2001.trackandfieldapp.repository.UserMapper;
import io.github.Toku2001.trackandfieldapp.service.password.impl.PasswordResetServiceImpl;
import io.github.Toku2001.trackandfieldapp.service.user.impl.MailServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@MapperScan("io.github.Toku2001.trackandfieldapp.repository")
public class ResetPasswordServiceTest {
	
	@Mock
	private  PasswordMapper passwordMapper;
	
	@Mock
	private  UserMapper userMapper;

	@InjectMocks
    private PasswordResetServiceImpl passwordResetServiceImpl;
	
	@Mock
    private MailServiceImpl mailServiceImpl;

	@Test
    void check_existUser_shouldReturnTrue() {
		//準備
		PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");
		//モックの設定
        when(userMapper.checkUser(anyString(), anyString())).thenReturn(1);
        //モックの設定
        when(passwordMapper.insert(any(PasswordResetToken.class))).thenReturn(1);
        //モック設定
        when(mailServiceImpl.sendPasswordResetMail(any(PasswordResetRequest.class), anyString())).thenReturn(true);
        // 実行
        boolean existUser = passwordResetServiceImpl.requestReset(request);
        assertEquals(true, existUser);
    }
	
	@Test
    void check_existUser_shouldReturnDatabaseOperationException() {
		//準備
		PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");
		//モックの設定
        when(userMapper.checkUser(anyString(), anyString())).thenReturn(0);
        // 実行
        DatabaseOperationException thrown = assertThrows(DatabaseOperationException.class, () -> {
        	passwordResetServiceImpl.requestReset(request);
        });
        assertTrue(thrown.getMessage().contains("ユーザー登録が未完了のため登録を完了してください。"));
    }
	
	@Test
    void check_insertCount_shouldReturnZero() {
		//準備
		PasswordResetRequest request = new PasswordResetRequest("mockName", "mock@example.com");
		//モックの設定
        when(userMapper.checkUser(anyString(), anyString())).thenReturn(1);
        //モックの設定
        when(passwordMapper.insert(any(PasswordResetToken.class))).thenReturn(1);
        //モック設定
        when(mailServiceImpl.sendPasswordResetMail(any(PasswordResetRequest.class), anyString())).thenReturn(false);
        // 実行
        boolean existUser = passwordResetServiceImpl.requestReset(request);
        assertEquals(false, existUser);
    }
}
