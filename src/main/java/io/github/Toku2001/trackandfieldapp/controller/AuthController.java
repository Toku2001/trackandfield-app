package io.github.Toku2001.trackandfieldapp.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
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
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
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
    	try {
            return loginService.login(request);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
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
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            // 最初のエラーメッセージを取得して返す例
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
        }
    	try {
            int registerNumber = registerUserService.registerUser(request);
            return ResponseEntity.ok(new RegisterResponse(request.getUserName(), registerNumber));
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", e.getMessage()));
        }
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