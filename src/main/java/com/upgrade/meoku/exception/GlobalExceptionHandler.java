package com.upgrade.meoku.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice는 Spring MVC의 컨트롤러에서 발생한 예외를 처리하기 때문에 그 이전인 필터(인증, 인가)는 처리 안함
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 모든 예외를 처리하는 글로벌 예외 핸들러 : 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
