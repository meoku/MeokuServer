package com.upgrade.meoku.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class MeokuSecurityConfig {

    private static final String[] AUTH_EXCLOUD_LIST = {
            "/api/v1/auth" //로그인, 회원가입 관련
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public MeokuSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // 새로운 방식으로 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTH_EXCLOUD_LIST).permitAll()
                .anyRequest().authenticated()
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
