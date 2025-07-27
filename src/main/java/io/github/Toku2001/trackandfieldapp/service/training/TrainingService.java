package io.github.Toku2001.trackandfieldapp.service.training;

import java.time.LocalDate;

import io.github.Toku2001.trackandfieldapp.dto.training.TrainingResponse;

public interface TrainingService{
	TrainingResponse getTraining(LocalDate TrainingDate);
}
