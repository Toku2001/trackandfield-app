package io.github.Toku2001.trackandfieldapp.dto.jump;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JumpResponse {
	private long jumpEventId;
    private String event;
    private Double distance;
    private Integer approach;
    private String poleFeat;
    private String polePond;
    private String recordType;
    private String jumpDate;  // yyyy-MM-dd
}