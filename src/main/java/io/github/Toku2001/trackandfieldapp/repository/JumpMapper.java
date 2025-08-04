package io.github.Toku2001.trackandfieldapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.dto.jump.ChangeJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.jump.RegisterJumpRequest;
import io.github.Toku2001.trackandfieldapp.entity.Jump_Events;

@Mapper
public interface JumpMapper {
    // 跳躍記録の登録
	int registerJump(@Param("userId") long userId, @Param("event") RegisterJumpRequest event);
	
	//跳躍記録の取得
	List<Jump_Events> getJumpRecord(@Param("userId") Long userId, @Param("date") LocalDate date);
	
	// 跳躍記録の更新
	void changeJump(@Param("userId") long userId, @Param("event") ChangeJumpRequest event);
	
	//跳躍記録の一部削除
	int deleteById(@Param("jumpEventId") Long jumpEventId);
	
	//跳躍記録の全削除
	int deleteAllByDate(@Param("jumpDate") LocalDate jumpDate);
	
	//自己ベスト取得
	List<Jump_Events> getPersonalBest(@Param("userId") long userId);
}