package io.github.Toku2001.trackandfieldapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Toku2001.trackandfieldapp.dto.competition.RegisterCompetitionRequest;
import io.github.Toku2001.trackandfieldapp.dto.jump.RegisterJumpRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.RegisterTrainingRequest;
import io.github.Toku2001.trackandfieldapp.service.competition.RegisterCompetitionService;
import io.github.Toku2001.trackandfieldapp.service.jump.RegisterJumpService;
import io.github.Toku2001.trackandfieldapp.service.training.RegisterTrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {
	private final RegisterTrainingService registerTrainingService;
	private final RegisterCompetitionService registerCompetitionService;
	private final RegisterJumpService registerJumpService;
	
	@PostMapping("/register-training")
	public ResponseEntity<Map<String, Object>> registerTraining(@Valid @RequestBody RegisterTrainingRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            // 最初のエラーメッセージを取得して返す例
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
        }
		try {
			int registerNumber = registerTrainingService.registerTraining(request);
			Map<String, Object> response = new HashMap<>();
	    	response.put("result", registerNumber);
			return ResponseEntity.ok(response);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(409).body(Map.of("error", e.getMessage())); // 409 Conflict
	    }
	}
	
	@PostMapping("/register-competition")
	public  ResponseEntity<?> registerCompetition(@Valid @RequestBody RegisterCompetitionRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            // 最初のエラーメッセージを取得して返す例
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
        }
		try {
	        int result = registerCompetitionService.registerCompetition(request);
	        return ResponseEntity.ok(result);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(409).body(Map.of("error", e.getMessage())); // 409 Conflict
	    }
	}
	
	@PostMapping("/register-jump-records")
    public ResponseEntity<?> registerJumpRecords(@RequestBody List<RegisterJumpRequest> records) {
    	registerJumpService.registerJump(records);
    	return ResponseEntity.ok(Map.of("result", 1));
    }
}
