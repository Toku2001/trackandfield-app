package io.github.Toku2001.trackandfieldapp.dto.jump;



import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterJumpRequest {

    @NotBlank(message = "種目名は必須です")
    private String event; //種目
    @NotNull(message = "記録は必須です")
    private Double distance; //記録
    @NotNull(message = "助走歩数は必須です")
    private Integer approach; //助走歩数
    private String poleFeat; // 例: 4.60
    private String polePond; // 例: 160
    private String recordType; //練習・競技会
    private LocalDate jumpDate;

}