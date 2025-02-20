package com.upgrade.meoku.security;

import com.upgrade.meoku.user.MeokuAuthService;
import com.upgrade.meoku.user.MeokuUserDetailsService;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

// Security Config에서 설정한 대로 동작함!
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 인증을 하지 않는 요청들
    private static final String[] AUTH_EXCLOUD_LIST = {
            "/swagger-ui/**" // swagger 관련
            ,"/v3/api-docs/**"
            ,"/swagger-resources/**"
            ,"/webjars/**"
            ,"/api/v1/auth/*" //로그인, 회원가입 관련
            ,"/api/v1/meokumenu/weekdaysmenu" //메뉴조회
            ,"/api/v1/meokumenu/searchMenuTag" //메뉴태그조회
            ,"/api/v1/meoku/getCurrentWeatherData" //날씨 데이터 조회
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

        // 헤더에 토큰이 존재하지 않으면 401 에러 반환
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        //"Baarer "이후의 토큰 값만 추출
        String token = header.substring(7);
        try {
            // 토큰 유효성 검증 (유효기간 등 )
            String id = jwtUtil.validateToken(token);
            // 유저 확인
            UserDetails meokuUserDetails = jwtUtil.extractUserDetailsFromJwt(token);

            // SecurityContext에 인증 정보 저장 (SecurityContext는 세션과 다르게 현재 요청 범위에서만 존재하며 이는 요청이 끝나면 사라짐)
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(meokuUserDetails, null, meokuUserDetails.getAuthorities()));
        } catch (ExpiredJwtException e) { // 인증시간 만료
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT 토큰이 만료되었습니다.");
            return;
        } catch (JwtException | IllegalArgumentException e) {// 올바르지 않은 토큰
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("잘못된 JWT 토큰입니다");

            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            // 그 외의 예기치 못한 에러는 500 에러 반환
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("서버 오류: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
