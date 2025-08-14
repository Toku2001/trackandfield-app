package io.github.Toku2001.trackandfieldapp.service.user;

import io.github.Toku2001.trackandfieldapp.dto.user.RegisterRequest;

public interface RegisterUserService {
	int registerUser(RegisterRequest request);
}
