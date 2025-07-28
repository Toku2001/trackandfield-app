package io.github.Toku2001.trackandfieldapp.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.Toku2001.trackandfieldapp.dto.training.DeleteTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.training.DeleteTrainingResponse;
import io.github.Toku2001.trackandfieldapp.service.training.DeleteTrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteController {
	private final DeleteTrainingService deleteTrainingService;
    
    @DeleteMapping("/delete-training")
    public ResponseEntity<?> deleteTraining(@Valid @RequestBody DeleteTrainingRequest request, BindingResult bindingResult){
    	if (bindingResult.hasErrors()) {
            // 最初のエラーメッセージを取得して返す例
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
        }
    	int deleteTrainingNumber = deleteTrainingService.deleteTraining(request); {
        if (deleteTrainingNumber == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return ResponseEntity.ok(new DeleteTrainingResponse(request.getTrainingDate(), deleteTrainingNumber));
    	}
    }
}
