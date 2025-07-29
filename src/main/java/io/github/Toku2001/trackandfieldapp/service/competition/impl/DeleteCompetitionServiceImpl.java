package io.github.Toku2001.trackandfieldapp.service.competition.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.DeleteCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.DeleteCompetitionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCompetitionServiceImpl implements DeleteCompetitionService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final CompetitionMapper competitionMapper;
	
	@Override
    public int deleteCompetition(DeleteCompetitionRequest request) {
		System.out.println("DeleteUserService received: " + request.getCompetitionDate());
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    int deleteCompetitionNumber = competitionMapper.deleteCompetition(userDetails.getUserId(), request.getCompetitionDate());
        if (deleteCompetitionNumber == 0) {
            throw new DatabaseOperationException("正しく競技会情報が削除されませんでした", new Exception());
        }
        return deleteCompetitionNumber;
    }
}
