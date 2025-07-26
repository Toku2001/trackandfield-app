package io.github.Toku2001.trackandfieldapp.service.password.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.password.NewPasswordRequest;
import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.entity.PasswordResetToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.PasswordMapper;
import io.github.Toku2001.trackandfieldapp.repository.UserMapper;
import io.github.Toku2001.trackandfieldapp.service.password.PasswordResetService;
import io.github.Toku2001.trackandfieldapp.service.user.MailService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

	private final PasswordMapper passwordMapper;
	private final UserMapper userMapper;
	private final MailService mailService;

	public void requestReset(PasswordResetRequest request){
		System.out.println(request.getUserMail());
		//パスワード再設定のリクエストをして生きたユーザーが登録されているユーザーか判定する
		int existUser = userMapper.checkUser(request.getUserName(), request.getUserMail());
		if(existUser == 0){			
			throw new DatabaseOperationException("ユーザー登録が未完了のため登録を完了してください。", new Exception());
		}
		String token = UUID.randomUUID().toString();
		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);	

		PasswordResetToken resetToken = new PasswordResetToken(request, token, expiryDate);
		passwordMapper.insert(resetToken);

		String resetLink = "http://localhost:8080/api/reset-password?token=" + token;
		mailService.sendPasswordResetMail(request, resetLink);
	}

	public boolean resetPassword(NewPasswordRequest request) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		Optional<PasswordResetToken> opt = passwordMapper.findUserMail(request.getUserName(), request.getToken());
		if (opt.isPresent() && opt.get().getExpiry_Date().isAfter(LocalDateTime.now())) {
			String user_Mail = opt.get().getUser_Mail();
			String hashedPassword = encoder.encode(request.getNewUserPassword());
			int updateNumber = passwordMapper.updateNewPassword(request.getUserName(), user_Mail, hashedPassword);
			if(updateNumber == 0) {
				throw new DatabaseOperationException("正常にパスワードを再設定できませんでした。", new Exception());
			}
			passwordMapper.deleteByToken(request.getToken());
			System.out.println("新パスワードで更新: " + user_Mail + " → " + request.getNewUserPassword());
			
			return true;
		}
		return false;
	}
}
