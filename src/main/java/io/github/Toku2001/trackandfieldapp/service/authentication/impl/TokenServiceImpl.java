package io.github.Toku2001.trackandfieldapp.service.authentication.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Toku2001.trackandfieldapp.dto.authentication.TokenResponse;
import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import io.github.Toku2001.trackandfieldapp.entity.User_Info;
import io.github.Toku2001.trackandfieldapp.exception.DatabaseOperationException;
import io.github.Toku2001.trackandfieldapp.repository.TokenMapper;
import io.github.Toku2001.trackandfieldapp.security.JwtUtil;
import io.github.Toku2001.trackandfieldapp.service.authentication.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
	private final TokenMapper tokenMapper;
	private final JwtUtil jwtUtil;
	
	@Override
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("無効なリフレッシュトークン");
        }
		Long userId = jwtUtil.extractUserId(refreshToken);
        String userName = jwtUtil.extractUserName(refreshToken);
        String userRole = jwtUtil.extractUserRole(refreshToken);
        String storedToken = tokenMapper.findTokenByUserInfo(userId.intValue(), userName);
        if (!refreshToken.equals(storedToken)) {
            throw new RuntimeException("不正なリフレッシュトークン");
        }
        
        //新しいトークン発行
        User_Info user_Info = new User_Info();
        user_Info.setUser_Id(userId.intValue());
        user_Info.setUser_Name(userName);
        user_Info.setUser_Role(userRole);
        String newAccessToken = jwtUtil.generateAccessToken(user_Info);
        String newRefreshToken = jwtUtil.generateRefreshToken(user_Info);

        //DB更新
        int updateNumber = tokenMapper.updateRefreshToken(userId.intValue(), newRefreshToken);
        if(updateNumber == 0) {
        	throw new DatabaseOperationException("パスワード更新件数が0件です。", new Exception());
        }
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
	
	@Override
	public String accessToken(String refreshToken) {
		Long userId = jwtUtil.extractUserId(refreshToken);
        String userName = jwtUtil.extractUserName(refreshToken);
        String userRole = jwtUtil.extractUserRole(refreshToken);
        String storedToken = tokenMapper.findTokenByUserInfo(userId.intValue(), userName);
        if (!refreshToken.equals(storedToken)) {
            throw new RuntimeException("不正なリフレッシュトークン");
        }
        User_Info user_Info = new User_Info();
        user_Info.setUser_Id(userId.intValue());
        user_Info.setUser_Name(userName);
        user_Info.setUser_Role(userRole);
        
        String newAccessToken = jwtUtil.generateAccessToken(user_Info);
		return newAccessToken;
	}
	
	@Override
	public int deleteRefreshToken() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsForToken userDetails = (UserDetailsForToken) authentication.getPrincipal();
        long userId = userDetails.getUserId();
        int deleteNumber = tokenMapper.deleteTokenByUserId((int)userId);
        if(deleteNumber == 0) {
        	throw new DatabaseOperationException("リフレッシュトークンが正常に削除できませんでした。", new Exception());
        }
        return deleteNumber;
	}
}
