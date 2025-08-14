package io.github.Toku2001.trackandfieldapp.service.jump.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.jump.ChangeJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.JumpMapper;
import io.github.Toku2001.trackandfieldapp.service.jump.ChangeJumpService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeJumpServiceImpl implements ChangeJumpService{

	private final JumpMapper jumpMapper;
	
	@Override
	public int changeJump(List<ChangeJumpRequest> changeJumpRequest){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    long userId = userDetails.getUserId();
	    //更新レコード
	    int changeJumpRecordNumber = 0;
	    try {
			for(ChangeJumpRequest target : changeJumpRequest) {
				int updatedCount = jumpMapper.changeJump(userId, target);
            	changeJumpRecordNumber += updatedCount;
			}
			if(changeJumpRecordNumber == changeJumpRequest.size()) {
				return 1;
			}
	    }catch(DatabaseOperationException e) {
	    	throw new DatabaseOperationException("跳躍情報の更新件数が0件です。", new Exception());
	    }
		return 0;
	}
}
