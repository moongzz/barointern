package com.moongoeun.project.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    String getCode();
    String getMessage();
    HttpStatus getStatus();
}
