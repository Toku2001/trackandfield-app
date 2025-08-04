package io.github.Toku2001.trackandfieldapp.service.training.impl;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.Training_Info;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TrainingMapper;
import io.github.Toku2001.trackandfieldapp.service.training.TrainingService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService{
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final TrainingMapper trainingMapper;
	
	@Override
	public TrainingResponse getTraining(LocalDate trainingDate) {
		System.out.println("TrainingService received: " + trainingDate + "の練習日誌を取得します。");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
		Training_Info training_Info = trainingMapper.getTrainingByDate(userDetails.getUserId(),trainingDate);
        if (training_Info == null) {
            throw new DatabaseOperationException("想定のデータが取得できていません。", new Exception());
        }
        TrainingResponse trainingResonse = new TrainingResponse();
        trainingResonse.setTrainingId(training_Info.getTraining_Id());
        trainingResonse.setTrainingTime(training_Info.getTraining_Time());
        trainingResonse.setTrainingPlace(training_Info.getTraining_Place());
        trainingResonse.setTrainingComments(training_Info.getTraining_Comments());
        return trainingResonse;
    }
}
