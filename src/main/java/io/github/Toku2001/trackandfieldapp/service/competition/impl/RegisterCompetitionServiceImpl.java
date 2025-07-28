package io.github.Toku2001.trackandfieldapp.service.competition.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.RegisterCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.RegisterCompetitionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCompetitionServiceImpl implements RegisterCompetitionService{
	private final CompetitionMapper competitionMapper;
    
	@Override
    public int registerCompetition(RegisterCompetitionRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
        int registerNumber = competitionMapper.registerCompetition(userDetails.getUserId(),
        													 request.getCompetitionName(),
        													 request.getCompetitionPlace(),
        													 request.getCompetitionTime(),
        													 request.getCompetitionComments());
        if (registerNumber == 0) {
            throw new DatabaseOperationException("練習日誌を登録できませんでした", new Exception());
        }
        return registerNumber;
    }
}
