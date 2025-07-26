package io.github.Toku2001.trackandfieldapp.service.authentication;

import io.github.Toku2001.trackandfieldapp.dto.authentication.TokenResponse;

public interface TokenService {
	TokenResponse refreshAccessToken(String refreshToken);
	
	String accessToken(String refreshToken);
	
	int deleteRefreshToken();
}
