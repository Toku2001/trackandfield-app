package io.github.Toku2001.trackandfieldapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginRequest;
import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginResponse;
import io.github.Toku2001.trackandfieldapp.dto.password.NewPasswordRequest;
import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.RegisterRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.RegisterResponse;
import io.github.Toku2001.trackandfieldapp.service.authentication.TokenService;
import io.github.Toku2001.trackandfieldapp.service.password.PasswordResetService;
import io.github.Toku2001.trackandfieldapp.service.user.LoginService;
import io.github.Toku2001.trackandfieldapp.service.user.RegisterUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;
    private final TokenService tokenService;
    private final RegisterUserService registerUserService;
    private final PasswordResetService passwordResetService;
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
    	LoginResponse loginResponse = loginService.login(request);
        if (loginResponse == null) {
            System.out.println("Login failed: user not found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return loginResponse;
    }
    
    @PostMapping("/logout")
    public boolean logout() {
        int deleteNumber = tokenService.deleteRefreshToken();
    	if(deleteNumber == 0) {
    		System.out.println("logout failed: ログアウトが正常に完了しませんでした");
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    	}
    	return true;
    }

    @PostMapping("/register-user")
	public RegisterResponse registerUser(@RequestBody RegisterRequest request) {
		int registerNumber = registerUserService.registerUser(request);
		if(registerNumber == 0) {
			System.out.println("RegisterUser failed: ユーザー登録が正常に完了しませんでした");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
		return new RegisterResponse(request.getUserName(), registerNumber);
	}

    @PostMapping("/request-password-reset")
    public String requestReset(@RequestBody PasswordResetRequest request) {
        	passwordResetService.requestReset(request);
        return "再設定リンクを送信しました。";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody NewPasswordRequest request) {
        boolean success = passwordResetService.resetPassword(request);
        return success ? "パスワードが更新されました。" : "トークンが無効または期限切れです。";
    }
}