package com.upgrade.meoku.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice는 Spring MVC의 컨트롤러에서 발생한 예외를 처리하기 때문에 그 이전인 필터(인증, 인가)에서 에러는 처리 안함
@RestControllerAdvice
public class GlobalExceptionHandler {
    // JWT 관련 예외 핸들러 (잘못된 JWT 토큰) : 400
    @ExceptionHandler({ JwtException.class, IllegalArgumentException.class })
    public ResponseEntity<String> handleJwtException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // JWT 관련 예외 핸들러 (인증시간 만료) : 401
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰이 만료되었습니다.");
    }

    // 모든 예외를 처리하는 글로벌 예외 핸들러 : 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
