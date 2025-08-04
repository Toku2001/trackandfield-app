package io.github.Toku2001.trackandfieldapp.service.training.impl;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.DeleteTrainingService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteTrainingServiceImpl implements DeleteTrainingService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final TrainingMapper trainingMapper;
	
	@Override
    public int deleteTraining(LocalDate trainingDate) {
		System.out.println("DeleteUserService received: " + trainingDate);
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    int deleteTrainingNumber = trainingMapper.deleteTraining(userDetails.getUserId(), trainingDate);
        if (deleteTrainingNumber == 0) {
            throw new DatabaseOperationException("正しく練習日誌が削除されませんでした", new Exception());
        }
        return deleteTrainingNumber;
    }
}
