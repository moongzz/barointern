package com.moongoeun.project.user.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moongoeun.project.user.domain.entity.UserEntity;
import lombok.Builder;

@Builder
public class ResAuthPostJoinDTOApiV1 {
    @JsonProperty
    private User user;

    public static ResAuthPostJoinDTOApiV1 of(UserEntity userEntity) {
        return ResAuthPostJoinDTOApiV1.builder()
            .user(ResAuthPostJoinDTOApiV1.User.from(userEntity))
            .build();
    }

    @Builder
    public static class User {
        @JsonProperty
        private String username;
        @JsonProperty
        private String nickname;

        private static User from(UserEntity userEntity) {
            return User.builder()
                .username(userEntity.getUsername())
                .nickname(userEntity.getNickname())
                .build();
        }
    }
}
