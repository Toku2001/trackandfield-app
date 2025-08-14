package io.github.Toku2001.trackandfieldapp.service.jump.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.repository.JumpMapper;
import io.github.Toku2001.trackandfieldapp.service.jump.DeleteJumpService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteJumpServiceImpl implements DeleteJumpService {
	
	private final JumpMapper jumpMapper;
	// 指定した跳躍記録を削除する
	public int deleteById(Long jumpEventId) {
		return jumpMapper.deleteById(jumpEventId);
	}
	
	// 指定された日の跳躍記録を全て削除する
	public int deleteAllByDate(String jumpDate) {
		LocalDate selectDate = LocalDate.parse(jumpDate);
		return jumpMapper.deleteAllByDate(selectDate);
	}
}
