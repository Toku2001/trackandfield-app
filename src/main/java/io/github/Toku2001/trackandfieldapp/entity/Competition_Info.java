package io.github.Toku2001.trackandfieldapp.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Competition_Info {
    private long competition_Id;
    private long user_Id;
    private String competition_Name;
    private String competition_Place;
    private LocalDate competition_Time;
    private String competition_Comments;
}