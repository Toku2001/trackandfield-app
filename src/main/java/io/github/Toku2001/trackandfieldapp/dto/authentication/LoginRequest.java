package io.github.Toku2001.trackandfieldapp.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class LoginRequest {
    private String userName;
    private String userPassword;
}