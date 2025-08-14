package io.github.Toku2001.trackandfieldapp.service.jump.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.jump.RegisterJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.repository.JumpMapper;
import io.github.Toku2001.trackandfieldapp.service.jump.RegisterJumpService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterJumpServiceImpl implements RegisterJumpService{

	private final JumpMapper jumpMapper;
	
	@Override
	public void registerJump(List<RegisterJumpRequest> registerJumpRequest){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    long userId = userDetails.getUserId();
	    
		for(RegisterJumpRequest target : registerJumpRequest) {
			jumpMapper.registerJump(userId, target);
		}
	}
}
