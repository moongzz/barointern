package com.moongoeun.project.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthGetRefreshDTOApiV1 {
    private String accessToken;

    public static ResAuthGetRefreshDTOApiV1 of(String accessToken) {
        return ResAuthGetRefreshDTOApiV1.builder()
            .accessToken(accessToken)
            .build();
    }
}
