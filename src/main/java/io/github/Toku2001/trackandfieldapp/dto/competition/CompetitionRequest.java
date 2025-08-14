package io.github.Toku2001.trackandfieldapp.dto.competition;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class CompetitionRequest {
    private String competition_name;
    private String competition_place;
    private Date competition_time;
    private String competition_comments;
//    private JumpRecordRequestDto jumpRecord;
}