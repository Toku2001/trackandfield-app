package io.github.Toku2001.trackandfieldapp.dto.competition;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class DeleteCompetitionRequest {
    private LocalDate competitionDate;
}