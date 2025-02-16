package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원인증 컨트롤러", description = "로그인, 회원가입 인증 절차 ")
@RestController
@RequestMapping("/api/v1/auth")
public class MeokuAuthController {

    MeokuAuthService meokuAuthService;
    public MeokuAuthController(MeokuAuthService meokuAuthService){
        this.meokuAuthService = meokuAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getMemberProfile(@Valid @RequestBody MeokuLoginRequestDTO request) {
        Map<String, Object> tokenMap = meokuAuthService.login(request);
        return ResponseEntity.ok(tokenMap);
    }

    @Operation(summary = "Refresh token으로 Access token 갱신", description = "우선 Access token, Refresh Token 갱신", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String refreshToken) {
        refreshToken = refreshToken.trim(); // 앞뒤 공백 제거
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7).trim(); // "Bearer " 제거하고 공백 제거
        }
        Map<String, Object> tokenMap = meokuAuthService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokenMap);
    }
}
