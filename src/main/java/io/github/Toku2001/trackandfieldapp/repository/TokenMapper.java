package io.github.Toku2001.trackandfieldapp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenMapper {
    // 既存ユーザー向けにリフレッシュトークンを更新
    int updateRefreshToken(@Param("userId") long userId,
                           @Param("refreshToken") String refreshToken);

    // 指定ユーザーのトークンを取得
    String findTokenByUserInfo(@Param("userId") long userId,
    						   @Param("userName") String userName);

    // リフレッシュトークンの削除（ログアウト時など）
    int deleteTokenByUserId(@Param("userId") long userId);
}
