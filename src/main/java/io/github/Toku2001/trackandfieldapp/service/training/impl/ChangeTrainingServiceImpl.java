package io.github.Toku2001.trackandfieldapp.service.training.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.training.ChangeTrainingRequest;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.ChangeTrainingService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeTrainingServiceImpl implements ChangeTrainingService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final TrainingMapper trainingMapper;
	
	@Override
    public int changeTraining(ChangeTrainingRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
	    int updateTrainingNumber = trainingMapper.changeTraining(userDetails.getUserId(),
				 request.getTrainingTime(),
				 request.getTrainingPlace(),
				 request.getTrainingComments());

		    if (updateTrainingNumber == 0) {
		        throw new DatabaseOperationException("練習日誌の更新件数が0件です。", new Exception());
		    }
		    return updateTrainingNumber;
		}
    }
