package io.github.Toku2001.trackandfieldapp.entity;

// JPAアノテーション
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")
public class User_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer user_Id;

    @Column(name = "user_name", nullable = false)
    private String user_Name;

    @Column(name = "user_password", nullable = false)
    private String user_Password;

    @Column(name = "user_mail", nullable = false, unique = true)
    private String user_Mail;

    @Column(name = "user_role")
    private String user_Role;

    @Column(name = "refresh_token")
    private String refresh_Token;
}