package io.github.Toku2001.trackandfieldapp.dto.training;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterTrainingRequest {
	@NotBlank(message = "練習時間は必須です")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "練習時間はYYYY-MM-DDの形式で記述してください")
    private String trainingTime;
	@NotBlank(message = "練習場所は必須です")
	@Size(max = 64, message = "練習場所は64文字以内で記入してください")
    private String trainingPlace;
	@Size(max = 255, message = "練習コメントは255文字以内で記入してください")
    private String trainingComments;
//    private JumpRecordRequestDto jumpRecord;
}