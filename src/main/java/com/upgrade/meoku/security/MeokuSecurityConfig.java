package com.upgrade.meoku.security;

import com.upgrade.meoku.user.MeokuUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity  // ✅ 이게 없으면 PreAuthrize 작동 안함! (메소드 보안 활성화 필수!)
@AllArgsConstructor
public class MeokuSecurityConfig {
    private final MeokuUserDetailsService meokuUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtil jwtUtil;

    // SecurityFilterChain: 보안 정책을 설정하는 곳 (허용할 경로, 인증 필터 등)
    // JWT 필터: 실제 인증을 처리하는 곳 (사용자 요청에 대한 JWT 토큰 확인)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // 새로운 방식으로 CSRF 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // ❌ HTTP 기본 인증 비활성화 (jwt와 같이 쓰면 충돌하여 혼란이 올 수 있음!)
                .formLogin(AbstractHttpConfigurer::disable) // ❌ 폼 로그인 비활성화 (폼로그인은 UsernamePasswordAuthenticationFilter 을 실행시키는데 우리는 jwt 인증을 하니 확인 할 필요가 없음)
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll() // 모두 허용하는 이유가 @PreAuthorize로 이 필터가 끝난 후 AOP로 인가를 할 예정이기 때문
                    //.anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth // 소셜로그인관련
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // ✅ 소셜 로그인 후 가져온 code로 access_token 발급 받고 사용자 정보 요청한 뒤 customOAuth2UserService에 넘겨줌
                        )
        );
        // 메인 필터 체인이 시작하기 전 인증을 담당하는 jwt 필터를 앞에 배치하여 실행(UsernamePasswordAuthenticationFilter가 다음 실행돼야 하기 때문에 인자로 넣지만 위에 disable 시켜서 수행되지는 않음)
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
