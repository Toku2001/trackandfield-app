package io.github.Toku2001.trackandfieldapp.service.user;

import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginRequest;
import io.github.Toku2001.trackandfieldapp.dto.authentication.LoginResponse;

public interface LoginService {
	LoginResponse login(LoginRequest request);
}
