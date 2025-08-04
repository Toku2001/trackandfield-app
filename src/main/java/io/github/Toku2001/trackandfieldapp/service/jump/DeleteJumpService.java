package io.github.Toku2001.trackandfieldapp.service.jump;

public interface DeleteJumpService {
	// 指定した跳躍記録を削除する
	int deleteById(Long jumpEventId);
	
	// 指定された日の跳躍記録を全て削除する
	int deleteAllByDate(String jumpDate);
}
