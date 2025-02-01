package com.upgrade.meoku.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

// Security Config에서 설정한 대로 동작함!
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final String[] AUTH_EXCLOUD_LIST = {
            "/swagger-ui/**" // swagger 관련
            ,"/v3/api-docs/**"
            ,"/swagger-resources/**"
            ,"/webjars/**"
            ,"/api/v1/auth/*" //로그인, 회원가입 관련
            ,"/api/v1/meokumenu/weekdaysmenu" //메뉴조회
            ,"/api/v1/meokumenu/searchMenuTag" //메뉴태그조회
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();

        // AntPathMatcher를 사용해서 예외 목록과 매칭
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isExcluded = Arrays.stream(AUTH_EXCLOUD_LIST)
                .anyMatch(url -> pathMatcher.match(url, requestURI));  // AntPathMatcher로 정확한 경로 매칭

        // 예외 목록에 포함된 경로는 JWT 필터를 건너뛰기
        if (isExcluded) {
            filterChain.doFilter(request, response);
            return;
        }
        // Swagger UI와 관련된 요청은 인증을 건너뛰기
        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);  // 바로 필터 체인으로 넘기기
            return;
        }

        // 헤더에 토큰이 존재하지 않으면 401 에러 반환
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        //"Baarer "이후의 토큰 값만 추출
        String token = header.substring(7);
        try {
            // 토큰 유효성 검증
            String username = jwtUtil.validateToken(token);
            // SecurityContext에 인증 정보 저장 (SecurityContext는 세션과 다르게 현재 요청 범위에서만 존재하며 이는 요청이 끝나면 사라짐)
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(username, null, null));
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
