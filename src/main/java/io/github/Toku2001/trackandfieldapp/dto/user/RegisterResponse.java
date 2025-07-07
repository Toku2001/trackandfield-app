package io.github.Toku2001.trackandfieldapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterResponse {
    private String userName;
    private int registerNumber;
}