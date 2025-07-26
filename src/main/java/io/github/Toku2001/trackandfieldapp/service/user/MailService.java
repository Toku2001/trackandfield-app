package io.github.Toku2001.trackandfieldapp.service.user;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;

public interface MailService {
	//パスワード再設定メールの送信
	public void sendPasswordResetMail(PasswordResetRequest request, String resetLink);
}
