package io.github.Toku2001.trackandfieldapp.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.Toku2001.trackandfieldapp.dto.competition.ChangeCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.jump.ChangeJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingResponse;
import io.github.Toku2001.trackandfieldapp.service.competition.ChangeCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.jump.ChangeJumpService;
import io.github.Toku2001.trackandfieldapp.service.training.ChangeTrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChangeController {
    private final ChangeTrainingService changeTrainingSerivice;
    private final ChangeCompetitionService changeCompetitionService;
    private final ChangeJumpService changeJumpService;
    
    @PutMapping("/change-training")
    public ResponseEntity<?> changeTraining(@Valid @RequestBody ChangeTrainingRequest request, BindingResult bindingResult){
    	if (bindingResult.hasErrors()) {
            // 最初のエラーメッセージを取得して返す例
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
        }
        int changeTrainingNumber = changeTrainingSerivice.changeTraining(request);
        if(changeTrainingNumber == 0) {
        	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
        return ResponseEntity.ok(new ChangeTrainingResponse(request.getTrainingTime(), request.getTrainingPlace(), request.getTrainingComments()));
    }
    
    @PutMapping("/change-competition")
    public int changeCompetition(@RequestBody ChangeCompetitionRequest request){
        int changeCompetitionNumber = changeCompetitionService.changeCompetition(request);
        if(changeCompetitionNumber == 0) {
			System.out.println("RegisterCompetition failed: 競技会情報の変更が正常に完了しませんでした");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
		}
        return changeCompetitionNumber;
    }
    
    @PutMapping("/change-jump-records")
    public int changeJumpRecord(@RequestBody List<ChangeJumpRequest> request){
        int changeJumpNumber = changeJumpService.changeJump(request);
        if(changeJumpNumber == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
		}
        return changeJumpNumber;
    }
}
