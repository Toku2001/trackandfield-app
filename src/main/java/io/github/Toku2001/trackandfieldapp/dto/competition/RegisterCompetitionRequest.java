package io.github.Toku2001.trackandfieldapp.dto.competition;



import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterCompetitionRequest {
	@NotBlank(message = "競技会名は必須です")
	@Size(max = 32, message = "競技会名は32文字以内で記入してください")
    private String competitionName;
	@NotBlank(message = "競技会開催場所は必須です")
	@Size(max = 32, message = "競技会開催場所は32文字以内で記入してください")
    private String competitionPlace;
	@NotNull(message = "競技会時間は必須です")
    private LocalDate competitionTime;
	@NotBlank(message = "競技会コメントは必須です")
	@Size(max = 255, message = "競技会コメントは255文字以内で記入してください")
    private String competitionComments;
//    private JumpRecordRequestDto jumpRecord;
}