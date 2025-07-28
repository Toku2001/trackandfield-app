package io.github.Toku2001.trackandfieldapp.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.service.competition.CompetitionService;
import io.github.Toku2001.trackandfieldapp.service.training.TrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadController {
	private final TrainingService trainingService;
	private final CompetitionService competitionService;
	
	@GetMapping("/get-training")
	public TrainingResponse readTraining(@RequestParam("trainingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate) {
		return trainingService.getTraining(trainingDate);
	}
	
	@GetMapping("/get-competition")
	public CompetitionResponse getCompetitionByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate competitionDate) {
		CompetitionResponse competitionResponse = competitionService.getCompetitionByDate(competitionDate);
        if(competitionResponse == null) {
        	throw new DatabaseOperationException("コントローラにデータが届いていません。");
        }
		return competitionResponse;
	}
	
	@GetMapping("/get-competition-upcoming")
	public CompetitionResponse getNextCompetition() {
		CompetitionResponse competitionResponse = competitionService.getNextCompetition();
        if(competitionResponse == null) {
        	throw new DatabaseOperationException("コントローラにデータが届いていません。");
        }
		return competitionResponse;
	}
}
