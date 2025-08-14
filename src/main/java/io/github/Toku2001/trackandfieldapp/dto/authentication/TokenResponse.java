package io.github.Toku2001.trackandfieldapp.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
	private String AccessToken;
	private String RefreshToken;
}
