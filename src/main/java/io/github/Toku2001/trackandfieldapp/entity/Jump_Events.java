package io.github.Toku2001.trackandfieldapp.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jump_Events {
	private Long jump_event_id;
    private Long user_id;
    private String event_code;
    private Double jump_distance;
    private Integer approach_steps;
    private String pole_length;
    private String pole_stiffness;
    private String record_type;
    private LocalDate jump_date;
    private LocalDateTime insert_date;
    private LocalDateTime update_date;
}