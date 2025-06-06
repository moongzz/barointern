package com.moongoeun.project.user.presentation.controller;

import com.moongoeun.project.global.response.ResDTO;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.dto.response.ResAuthGetRefreshDTOApiV1;
import com.moongoeun.project.user.application.dto.response.ResAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.service.AuthServiceApiV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthControllerApiV1 {

    private final AuthServiceApiV1 authServiceApiV1;

    @PostMapping("/join")
    public ResponseEntity<ResDTO<ResAuthPostJoinDTOApiV1>> joinBy(
        @Valid
        @RequestBody ReqAuthPostJoinDTOApiV1 dto
    ) {
        ResAuthPostJoinDTOApiV1 data = authServiceApiV1.joinBy(dto);

        return new ResponseEntity<>(
            ResDTO.<ResAuthPostJoinDTOApiV1>builder()
                .code("0")
                .message("회원가입에 성공하였습니다.")
                .data(data)
                .build(),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResDTO<ResAuthGetRefreshDTOApiV1>> refreshBy(
        @RequestParam String accessToken,
        @RequestParam String refreshToken
    ) {
        ResAuthGetRefreshDTOApiV1 newAccessToken = authServiceApiV1.refreshBy(accessToken,
            refreshToken);

        return new ResponseEntity<>(
            ResDTO.<ResAuthGetRefreshDTOApiV1>builder()
                .code("0")
                .message("Access Token 재발급 성공했습니다.")
                .data(newAccessToken)
                .build(),
            HttpStatus.OK
        );
    }
}
