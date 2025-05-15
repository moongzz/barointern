package com.moongoeun.project.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostLoginDTOApiV1 {
    private String accessToken;
    private String refreshToken;

    public static ResAuthPostLoginDTOApiV1 of(String accessToken, String refreshToken) {
        return ResAuthPostLoginDTOApiV1.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
