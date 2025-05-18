package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuSignUpRequestDTO;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "회원가입", description = "일반유저 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MeokuSignUpRequestDTO signUpRequestDTO) {
        MeokuUserDTO savedUserDto;
        try{
            savedUserDto = meokuAuthService.signup(signUpRequestDTO);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("회원가입 성공! ID: " + savedUserDto.getId());
    }

    @Operation(summary = "id중복체크", description = "입력 형식 { \"checkedId\":\"아이디입력\"}, 반환 형식 {\"message\":메시지, \"available\": true or false}")
    @PostMapping("/checkDuplicateId")
    public ResponseEntity<?> checkDuplicateId(@RequestParam String checkedId){
        Map<String, Object> response = new HashMap<>();
        // 입력 유효성 검사
        if (!checkedId.matches("^[a-zA-Z0-9]{5,20}$")) {
            response.put("message", "ID는 5~20자의 영문 또는 숫자만 가능합니다.");
            response.put("available", false);
            return ResponseEntity.ok(response);
        }

        boolean isDuplicate = meokuAuthService.checkDuplicateId(checkedId);

        if (isDuplicate) {
            response.put("message", "이미 사용 중인 아이디입니다.");
            response.put("available", false);
        } else {
            response.put("message", "사용 가능한 아이디입니다.");
            response.put("available", true);
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh token으로 Access token 갱신", description = "파라메터 없고 헤더에 Authorization 으로 리프레시 토큰을 넣으면 access, refresh token 객체 반환. 이때 헤더에 리프레시 토큰이 아닌 엑세스 토큰 넣으면 에러 반환됨", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/refreshAccessToken")
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
