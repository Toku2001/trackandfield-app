package io.github.Toku2001.trackandfieldapp.dto.training;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class TrainingRequest {
    private Date trainingTime;
    private String trainingPlace;
    private String trainingComments;
//    private JumpRecordRequestDto jumpRecord;
}