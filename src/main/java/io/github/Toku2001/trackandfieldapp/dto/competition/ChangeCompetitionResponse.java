package io.github.Toku2001.trackandfieldapp.dto.competition;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ChangeCompetitionResponse {
    private String competitionName;
    private String competitionPlace;
    private Date competitionTime;
    private String competitionComments;
}