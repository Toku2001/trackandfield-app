package io.github.Toku2001.trackandfieldapp.service.competition.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.ChangeCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.ChangeCompetitionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeCompetitionServiceImpl implements ChangeCompetitionService{
	private final CompetitionMapper competitionMapper;
	
	@Override
    public int changeCompetition(ChangeCompetitionRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    int updateCompetitionNumber = competitionMapper.changeCompetition(userDetails.getUserId(),
	    															  request.getCompetitionId(),
	    															  request.getCompetitionName(),
																	  request.getCompetitionPlace(),
																	  request.getCompetitionTime(),
																	  request.getCompetitionComments());
		    if (updateCompetitionNumber == 0) {
		        throw new DatabaseOperationException("競技会情報の更新件数が0件です。", new Exception());
		    }
		    return updateCompetitionNumber;
		}
    }
