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
    UNSUPPORTED_JWT("A202", "지원되지 않는 JWT Token 입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_CLAIMS("A203", "잘못된 JWT Token 입니다.", HttpStatus.BAD_REQUEST),

    RESPONSE_WRITE_FAIL("A301", "응답을 작성하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    AUTHENTICATION_FAILED("A401", "인증에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH("A402", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_SUBJECT_MISMATCH("A403", "AccessToken과 RefreshToken의 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("A404", "만료된 Token입니다. 재로그인을 요청해주세요.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN("A405", "JWT가 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_SIGNATURE("A406", "유효하지 않은 JWT 서명 입니다.", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND("A501", "존재하지 않는 회원 아이디입니다.", HttpStatus.NOT_FOUND),

    ACCESS_DENIED("A601", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

}
