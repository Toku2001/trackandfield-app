package io.github.Toku2001.trackandfieldapp.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.Toku2001.trackandfieldapp.dto.training.DeleteTrainingResponse;
import io.github.Toku2001.trackandfieldapp.service.competition.DeleteCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.jump.DeleteJumpService;
import io.github.Toku2001.trackandfieldapp.service.training.DeleteTrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteController {
	private final DeleteTrainingService deleteTrainingService;
	private final DeleteCompetitionService deleteCompetitionService;
	private final DeleteJumpService deleteJumpService;
    
    @DeleteMapping("/delete-training")
    public ResponseEntity<?> deleteTraining(@RequestParam("trainingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate){
    	int deleteTrainingNumber = deleteTrainingService.deleteTraining(trainingDate); {
        if (deleteTrainingNumber == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return ResponseEntity.ok(new DeleteTrainingResponse(trainingDate, deleteTrainingNumber));
    	}
    }
    
    @DeleteMapping("/delete-competition")
    public int deleteCompetition(@RequestParam("competitionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate competitionDate){
    	int deleteCompetitionNumber = deleteCompetitionService.deleteCompetition(competitionDate); {
        if (deleteCompetitionNumber == 0) {
            System.out.println("deleteCompetition failed: training not found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return deleteCompetitionNumber;
    	}
    }
    
    @DeleteMapping("/delete-jump-record")
    public ResponseEntity<Void> deleteById(@RequestParam Long jumpEventId) {
    	int deletedCount = deleteJumpService.deleteById(jumpEventId);
        if (deletedCount > 0) {
            return ResponseEntity.noContent().build(); // 204 No Content：正常削除
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found：対象が存在しなかった
        }
    }
    
    @DeleteMapping("/delete-jump-records-by-date")
    public ResponseEntity<Void> deleteByDate(@RequestParam String jumpDate) {
    	int deletedCount = deleteJumpService.deleteAllByDate(jumpDate);
        if (deletedCount > 0) {
            return ResponseEntity.noContent().build(); // 204 No Content：正常削除
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found：対象が存在しなかった
        }
    }
}
