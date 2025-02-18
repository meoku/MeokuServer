package com.upgrade.meoku.security;

import com.upgrade.meoku.user.data.MeokuUserDTO;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtUtil {
    @Value("${JWT_SECRET_KEY}")
    private  String SECRET_KEY;
    @Value("${ACCESS_TOKEN_EXPIRATION_TIME}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${REFRESH_TOKEN_EXPIRATION_TIME}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private Key key;

    @PostConstruct // 객체 생성 후 자동 실행됨(Spring 컨텍스트가 완전히 초기화된 후 실행)
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public Map<String, Object> generateTokenMap(MeokuUserDTO userDTO) {

        String accessToken = this.generateAccessToken(userDTO);
        String refreshToken = this.generateRefreshToken(userDTO);

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", refreshToken);

        return tokenMap;
    }
    // JWT 토큰 생성 (로그인 시, 재 갱신시)
    public String generateAccessToken(MeokuUserDTO userDTO) {
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
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT Refresh 토큰 생성 (로그인 시)
    public String generateRefreshToken(MeokuUserDTO userDTO) {
        return Jwts.builder()
                .setSubject(userDTO.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰 검증 (요청으로 온 헤더에서 담긴 JWT로 인증)
    // 단일 책임의 원칙으로 에러 핸들링을 이를 사용하는 상위 로직에서 처리 하고 이곳에서는 단순하게 검증만 처리
    public String validateToken(String token) {

        Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) //서명 검증, 만료시간 검증, 기타 JWT 포멧 검증까지 함
                    .getBody();

            return claims.getSubject();
    }

    // Jwt Claim에서 내용 추출
    public MeokuUserDetails extractUserDetailsFromJwt(String token) {
        MeokuUserDTO meokuUserDTO = new MeokuUserDTO();

        // JWT에서 Claims 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject(); // ID 가져오기
        List<GrantedAuthority> authorities = getAuthorities(claims); // 권한 정보 가져오기

        meokuUserDTO.setId(username);
        meokuUserDTO.setUserRoleDTOList(null);

        return new MeokuUserDetails(meokuUserDTO);
    }

    // 추출한 Claim에서 권한 목록 가져오기
    private List<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
