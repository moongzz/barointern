package com.moongoeun.project.user.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moongoeun.project.global.exception.ExceptionResponse;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse body = new ExceptionResponse(
            AuthExceptionCode.ACCESS_DENIED.getCode(),
            AuthExceptionCode.ACCESS_DENIED.getMessage(),
            AuthExceptionCode.ACCESS_DENIED.getStatus().value()
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
