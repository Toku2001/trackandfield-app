package io.github.Toku2001.trackandfieldapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterRequest {
    private String userName;
    private String userPassword;
    private String userMail;
}