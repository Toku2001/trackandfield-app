package io.github.Toku2001.trackandfieldapp.service.password;

import io.github.Toku2001.trackandfieldapp.dto.password.NewPasswordRequest;
import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;

public interface PasswordResetService {
	//パスワード再設定のメール発行
	public boolean requestReset(PasswordResetRequest request);
	//新しいパスワードで再設定
	public boolean resetPassword(NewPasswordRequest request);
}
