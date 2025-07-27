package io.github.Toku2001.trackandfieldapp.service.training.impl;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.training.RegisterTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.RegisterTrainingService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterTrainingServiceImpl implements RegisterTrainingService{
	private final TrainingMapper trainingMapper;
    
	@Override
    public int registerTraining(RegisterTrainingRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    LocalDate localDate = LocalDate.parse(request.getTrainingTime());
	    LocalDate today = LocalDate.now();
        int registerNumber = trainingMapper.registerTraining(userDetails.getUserId(),
        													 today,
        													 request.getTrainingPlace(),
        													 request.getTrainingComments());
        if (registerNumber == 0) {
            throw new DatabaseOperationException("練習日誌を登録できませんでした", new Exception());
        }
        return registerNumber;
    }
}
