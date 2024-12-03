package com.upgrade.meoku.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "cRZkgSHTE+QbBe6FKaYKZGLKJKBJhPtLHooiXt1sUCI="; // 임시로 해놓음
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // JWT 토큰 생성 (로그인 시)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // JWT 토큰 검증 (요청으로 온 헤더에서 담긴 JWT로 인증)
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }
}
