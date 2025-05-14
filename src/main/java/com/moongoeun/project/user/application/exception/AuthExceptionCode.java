package com.moongoeun.project.user.application.exception;

import com.moongoeun.project.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
    DUPLICATE_USERNAME("A101", "이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("A102", "이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

}
