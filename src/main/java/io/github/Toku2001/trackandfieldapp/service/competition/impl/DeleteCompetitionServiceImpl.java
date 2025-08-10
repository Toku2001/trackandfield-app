package io.github.Toku2001.trackandfieldapp.service.competition.impl;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public int deleteCompetition(LocalDate competitionDate) {
		System.out.println("DeleteUserService received: " + competitionDate);
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    int deleteCompetitionNumber = competitionMapper.deleteCompetition(userDetails.getUserId(), competitionDate);
        if (deleteCompetitionNumber == 0) {
            throw new DatabaseOperationException("正しく競技会情報が削除されませんでした", new Exception());
        }
        return deleteCompetitionNumber;
    }
}
