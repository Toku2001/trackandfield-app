package io.github.Toku2001.trackandfieldapp.dto.jump;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeJumpRequest {
	private long jumpEventId;
    private String event;         // 種目（例: pole_vault）
    private double distance;      // 記録（メートル）
    private int approach;         // 助走の歩数
    private String poleFeat;      // ポールの長さ（例: "4.80"）
    private String polePond;      // ポールの硬さ（例: "170"）
    private String recordType;    // 記録の種別（training / competition）
    private LocalDate jumpDate;      // 記録日（例: "2025-08-02"）
}