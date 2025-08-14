package io.github.Toku2001.trackandfieldapp.dto.training;



import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ChangeTrainingRequest {
	private int trainingId;
	@NotNull(message = "更新したい練習日誌の日付が正しくリクエストされていません")
    private LocalDate trainingTime;
    private String trainingPlace;
    private String trainingComments;
//    private JumpRecordRequestDto jumpRecord;
}