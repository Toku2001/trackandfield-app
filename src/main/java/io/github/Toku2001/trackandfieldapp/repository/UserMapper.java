package io.github.Toku2001.trackandfieldapp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.entity.User_Info;

@Mapper
public interface UserMapper {
    //ログイン
	User_Info getUserInfo(
		@Param("userName") String userName
	);

	//ユーザー新規登録
    int registerUser(
		@Param("userName") String userName, 
		@Param("userPassword") String userPassword,
		@Param("userMail") String userMail
	);

	//パスワード再発行をリクエストしたユーザーがすでに登録されているユーザーであるかどうか判定する
	int checkUser(
		@Param("userName") String userName,
		@Param("userMail") String userMail
	);
}
