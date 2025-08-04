package io.github.Toku2001.trackandfieldapp.service.jump;

import java.util.List;

import io.github.Toku2001.trackandfieldapp.dto.jump.ChangeJumpRequest;

public interface ChangeJumpService {
	//跳躍記録の更新
	int changeJump(List<ChangeJumpRequest> changeJumpRequest);
}
