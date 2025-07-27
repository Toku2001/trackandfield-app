package io.github.Toku2001.trackandfieldapp.controller;

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

import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingResponse;
import io.github.Toku2001.trackandfieldapp.service.training.ChangeTrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChangeController {
    private final ChangeTrainingService changeTrainingSerivice;
    
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
}
