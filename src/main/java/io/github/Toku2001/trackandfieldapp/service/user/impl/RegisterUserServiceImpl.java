package io.github.Toku2001.trackandfieldapp.service.user.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.user.RegisterRequest;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.UserMapper;
import io.github.Toku2001.trackandfieldapp.service.user.RegisterUserService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterUserServiceImpl implements RegisterUserService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final UserMapper userMapper;
    
	@Override
    public int registerUser(RegisterRequest request) {
		System.out.println("RegisterService received: " + request.getUserName() + request.getUserMail());
		String hashedPassword = encoder.encode(request.getUserPassword());
		try {
			int registerNumber = userMapper.registerUser(request.getUserName(), hashedPassword ,request.getUserMail());
	        return registerNumber;
		}catch(Exception e) {
			throw new DatabaseOperationException("登録できませんでした", new Exception());
		}
    }
}
