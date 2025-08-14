package io.github.Toku2001.trackandfieldapp.dto.jump;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalBestResponse {
    private String event;
    private Double distance;
    private String jumpDate;
}