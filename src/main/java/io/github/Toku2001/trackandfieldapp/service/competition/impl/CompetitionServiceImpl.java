package io.github.Toku2001.trackandfieldapp.service.competition.impl;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Competition_Info;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.CompetitionMapper;
import io.github.Toku2001.trackandfieldapp.service.competition.CompetitionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final CompetitionMapper competitionMapper;
	
	@Override
	public CompetitionResponse getCompetitionByDate(LocalDate trainingDate) {
		System.out.println("CompetitionService received: " + trainingDate + "の練習日誌を取得します。");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    Competition_Info competition_Info = competitionMapper.getCompetitionByDate(userDetails.getUserId(), trainingDate);
        if (competition_Info == null) {
            throw new DatabaseOperationException("想定のデータが取得できていません。", new Exception());
        }
        CompetitionResponse competitionResponse = new CompetitionResponse();
        competitionResponse.setCompetitionName(competition_Info.getCompetition_Name());
        competitionResponse.setCompetitionPlace(competition_Info.getCompetition_Place());
        competitionResponse.setCompetitionTime(competition_Info.getCompetition_Time());
        competitionResponse.setCompetitionComments(competition_Info.getCompetition_Comments());
        return competitionResponse;
    }
	
	@Override
	public CompetitionResponse getNextCompetition() {
		Competition_Info competition_Info = competitionMapper.getNextCompetition();
        if (competition_Info == null) {
            return new CompetitionResponse();
        }
        CompetitionResponse competitionResponse = new CompetitionResponse();
        competitionResponse.setCompetitionName(competition_Info.getCompetition_Name());
        competitionResponse.setCompetitionPlace(competition_Info.getCompetition_Place());
        competitionResponse.setCompetitionTime(competition_Info.getCompetition_Time());
        return competitionResponse;
    }
}
