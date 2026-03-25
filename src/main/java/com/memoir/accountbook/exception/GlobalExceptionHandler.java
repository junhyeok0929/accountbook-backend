package com.memoir.accountbook.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        e.printStackTrace(); // 서버 콘솔에 에러 출력
        // 프론트엔드에 에러 메시지를 평문 문자열로 전달
        return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body("잘못된 요청: " + e.getMessage());
    }
}
