package io.github.Toku2001.trackandfieldapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class UserResponse {
    private Integer userId;
    private String userName;
    private String userMail;
    private String userRole;
}