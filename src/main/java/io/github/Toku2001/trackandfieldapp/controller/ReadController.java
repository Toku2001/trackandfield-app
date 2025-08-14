package io.github.Toku2001.trackandfieldapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.Toku2001.trackandfieldapp.dto.competition.CompetitionResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.JumpResponse;
import io.github.Toku2001.trackandfieldapp.dto.jump.PersonalBestResponse;
import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.service.competition.CompetitionService;
import io.github.Toku2001.trackandfieldapp.service.jump.JumpService;
import io.github.Toku2001.trackandfieldapp.service.training.TrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadController {
	private final TrainingService trainingService;
	private final CompetitionService competitionService;
	private final JumpService jumpService;
	
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
	
	@GetMapping("/get-jump-records")
    public ResponseEntity<List<JumpResponse>> getJumpRecords(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jumpDate) {
		System.out.println("リクエストで送られてきた日時は"+jumpDate);
        List<JumpResponse> records = jumpService.getJumpRecords(jumpDate);
        return ResponseEntity.ok(records);
    }
	
	@GetMapping("/get-competition-upcoming")
	public CompetitionResponse getNextCompetition() {
		CompetitionResponse competitionResponse = competitionService.getNextCompetition();
        if(competitionResponse == null) {
        	throw new DatabaseOperationException("コントローラにデータが届いていません。");
        }
		return competitionResponse;
	}
	
	@GetMapping("/get-personal-best")
	public List<PersonalBestResponse> getPersonalBest() {
		List<PersonalBestResponse> personalBestResponse = jumpService.getPersonalBest();
        if(personalBestResponse == null) {
        	throw new DatabaseOperationException("コントローラにデータが届いていません。");
        }
		return personalBestResponse;
	}
}
