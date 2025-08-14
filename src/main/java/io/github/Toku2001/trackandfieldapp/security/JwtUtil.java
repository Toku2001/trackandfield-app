package io.github.Toku2001.trackandfieldapp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.Toku2001.trackandfieldapp.entity.User_Info;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    // 秘密鍵
	@Value("${jwt.secret}")
    private String SecretKey;

    // 有効期限
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 10;//10分ほど
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 15; // 15分ほど

    public String generateAccessToken(User_Info userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUser_Id());
        claims.put("userRole", userInfo.getUser_Role());
        return createToken(claims, userInfo.getUser_Name(), ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(User_Info userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUser_Id());
        claims.put("userRole", userInfo.getUser_Role());
        return createToken(claims, userInfo.getUser_Name(), REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(Map<String, Object> claims, String subject, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)  // ユーザー名
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SecretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserRole(String token) {
        return extractAllClaims(token).get("userRole", String.class);
    }

    public Long extractUserId(String token) {
        Integer userId = extractAllClaims(token).get("userId", Integer.class);
        return userId != null ? userId.longValue() : null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SecretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}