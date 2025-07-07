package io.github.Toku2001.trackandfieldapp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.entity.User_Info;

@Mapper
public interface UserMapper {
    //ログイン
	User_Info getUserInfo(
		@Param("userName") String userName);
}
