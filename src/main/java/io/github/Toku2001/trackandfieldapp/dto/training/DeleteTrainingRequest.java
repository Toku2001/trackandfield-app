package io.github.Toku2001.trackandfieldapp.dto.training;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class DeleteTrainingRequest {
	@NotNull(message = "削除したい練習日誌の日付が正しくリクエストされていません")
    private LocalDate trainingDate;
}