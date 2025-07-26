package io.github.Toku2001.trackandfieldapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterRequest{
	@NotBlank(message = "ユーザー名は必須です")
	@Size(max = 16, message = "ユーザー名は16文字以内で入力してください")
    private String userName;
	@NotBlank(message = "パスワードは必須です")
	@Size(max = 16, message = "パスワードは16文字以内で入力してください")
    private String userPassword;
	@NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    private String userMail;
}