package io.github.Toku2001.trackandfieldapp.dto.training;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class DeleteTrainingResponse {
	private LocalDate deleteDate;
    private int deleteNumber;
}