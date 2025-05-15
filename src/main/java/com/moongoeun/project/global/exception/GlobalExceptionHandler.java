package com.moongoeun.project.global.exception;

import java.nio.file.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException exc) {
        ExceptionResponse body = new ExceptionResponse(exc.getCode(), exc.getMessage(),
            exc.getHttpStatus());
        return ResponseEntity.status(exc.getHttpStatus()).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException() {
        ExceptionResponse body = new ExceptionResponse("ACCESS_DENIED", "접근이 거부된 권한입니다.",
            HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}
