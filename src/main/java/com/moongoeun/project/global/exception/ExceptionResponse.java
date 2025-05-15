package com.moongoeun.project.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionResponse {
    private final String code;
    private final String message;
    private final int httpStatus;
}
