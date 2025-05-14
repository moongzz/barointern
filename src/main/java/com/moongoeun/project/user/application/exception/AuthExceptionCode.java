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

    INVALID_REQUEST_BODY("A201", "요청 바디를 JSON으로 파싱하지 못했습니다.", HttpStatus.BAD_REQUEST),

    RESPONSE_WRITE_FAIL("A301", "응답을 작성하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    AUTHENTICATION_FAILED("A401", "인증에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH("A402", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND("A501", "존재하지 않는 회원 아이디입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

}
