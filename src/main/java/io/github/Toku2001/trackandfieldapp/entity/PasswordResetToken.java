package io.github.Toku2001.trackandfieldapp.entity;

import java.time.LocalDateTime;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    private Integer token_Id;
    private String user_Name;
    private String user_Mail;
    private String token;
    private LocalDateTime expiry_Date;
    

    public PasswordResetToken(PasswordResetRequest request, String token, LocalDateTime expiryDate) {
    	this.user_Name = request.getUserName();
        this.user_Mail = request.getUserMail();
        this.token = token;
        this.expiry_Date = expiryDate;
    }
}