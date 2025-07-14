package io.github.Toku2001.trackandfieldapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_Info {
    private Integer user_Id;
    private String user_Name;
    private String user_Password;
    private String user_Mail;
    private String user_Role;
    private String refresh_Token;
}