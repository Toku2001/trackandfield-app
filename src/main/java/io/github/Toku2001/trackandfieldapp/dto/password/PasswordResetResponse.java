package io.github.Toku2001.trackandfieldapp.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponse {
	
	private boolean isSendLink;
	private String responseMessage;
	
}
