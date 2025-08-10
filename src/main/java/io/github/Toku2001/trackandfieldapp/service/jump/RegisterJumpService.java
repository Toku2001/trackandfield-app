package io.github.Toku2001.trackandfieldapp.service.jump;

import java.util.List;

import io.github.Toku2001.trackandfieldapp.dto.jump.RegisterJumpRequest;

public interface RegisterJumpService {
	//跳躍記録の登録
	void registerJump(List<RegisterJumpRequest> registerEventRequest);
}
