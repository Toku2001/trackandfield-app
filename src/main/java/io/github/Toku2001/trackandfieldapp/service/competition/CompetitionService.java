package io.github.Toku2001.trackandfieldapp.service.competition;

import java.time.LocalDate;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;

public interface CompetitionService{
	//日付指定で競技会情報を取得
	CompetitionResponse getCompetitionByDate(LocalDate trainingDate);
	//ログイン認証後に直近の競技会情報を取得する
	CompetitionResponse getNextCompetition();
}
