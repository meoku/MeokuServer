package com.upgrade.meoku.security;

import com.upgrade.meoku.user.data.MeokuUserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "cRZkgSHTE+QbBe6FKaYKZGLKJKBJhPtLHooiXt1sUCI="; // 임시로 해놓음
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 임시로 해놓음
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // JWT 토큰 생성 (로그인 시)
    public String generateToken(MeokuUserDTO userDTO) {
        // 권한 리스트 추출 : UserDTO의 권한List에서 권한 이름만 모아 따로 리스트로 추출
        List<String> roles = userDTO.getUserRoleDTOList().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toList());
        //Claims 생성 : id, roles 저장
        Claims claims = Jwts.claims().setSubject(userDTO.getId());
        claims.put("roles", roles); // 권한 필드 추가

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
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

            // 🔹 만료 시간 검증 추가
            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Expired JWT Token");  // ExpiredJwtException 던짐
            }
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw e;  // 인증시간 만료
        } catch (JwtException | IllegalArgumentException e) {
            throw e;  // 올바르지 않은 토큰
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);  // 예기치 못한 예외 처리
        }
    }
}
