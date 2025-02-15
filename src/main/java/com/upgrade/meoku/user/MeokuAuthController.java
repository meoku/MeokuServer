package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
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

    @PostMapping("login")
    public ResponseEntity<?> getMemberProfile(@Valid @RequestBody MeokuLoginRequestDTO request) {
        Map<String, Object> tokenMap = meokuAuthService.login(request);
        return ResponseEntity.ok(tokenMap);
    }
}
