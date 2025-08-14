package io.github.Toku2001.trackandfieldapp.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.entity.PasswordResetToken;

@Mapper
public interface PasswordMapper {
    Optional<PasswordResetToken> findUserMail(@Param("user_Name") String userName,
    										  @Param("token") String token);
    void deleteByToken(@Param("token") String token); 
    int insert(PasswordResetToken passwordResetToken);
    int updateNewPassword(@Param("user_Name") String userNames,
    				      @Param("user_Mail") String userMail,
    					  @Param("newUserPassword") String newUserPassword);
}
