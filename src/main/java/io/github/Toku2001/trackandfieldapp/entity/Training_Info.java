package io.github.Toku2001.trackandfieldapp.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training_Info {
    private Integer user_Id;
    private LocalDate training_Time;
    private String training_Place;
    private String training_Comments;
}