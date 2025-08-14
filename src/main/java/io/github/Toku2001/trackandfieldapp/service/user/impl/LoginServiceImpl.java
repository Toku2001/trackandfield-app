package io.github.Toku2001.trackandfieldapp.service.user.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginRequest;
import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginResponse;
import io.github.Toku2001.trackandfieldapp.entity.User_Info;
import io.github.Toku2001.trackandfieldapp.repository.TokenMapper;
import io.github.Toku2001.trackandfieldapp.repository.UserMapper;
import io.github.Toku2001.trackandfieldapp.security.JwtUtil;
import io.github.Toku2001.trackandfieldapp.service.user.LoginService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	private final UserMapper userMapper;
	private final TokenMapper tokenMapper;
	private final JwtUtil jwtUtil;
	
	@Override
	public LoginResponse login(LoginRequest request) {
	    System.out.println("LoginService received: " + request.getUserName());
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	    User_Info user_Info = userMapper.getUserInfo(request.getUserName());

	    if (user_Info == null) {
	        // ユーザーが存在しない場合
	        throw new BadCredentialsException("入力されたユーザー名またはパスワードが異なります。");
	    }

	    String hashedUserPassword = user_Info.getUser_Password();
	    boolean matches = encoder.matches(request.getUserPassword(), hashedUserPassword);

	    if (!matches) {
	        // パスワードが一致しない場合
	        throw new BadCredentialsException("入力されたユーザー名またはパスワードが異なります。");
	    }

	    String accessToken = jwtUtil.generateAccessToken(user_Info);
	    String refreshToken = jwtUtil.generateRefreshToken(user_Info);
	    int registerNumber = tokenMapper.updateRefreshToken(user_Info.getUser_Id(), refreshToken);

	    if (registerNumber != 1) {
	        throw new RuntimeException("トークン登録処理に失敗しました。");
	    }

	    return new LoginResponse(user_Info.getUser_Name(), accessToken);
	}
}
