package com.moongoeun.project.user.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.vo.RoleType;
import lombok.Builder;

@Builder
public class ResUserPatchAdminDTOApiV1 {
    @JsonProperty
    private User user;

    public static ResUserPatchAdminDTOApiV1 of(UserEntity userEntity) {
        return ResUserPatchAdminDTOApiV1.builder()
            .user(ResUserPatchAdminDTOApiV1.User.from(userEntity))
            .build();
    }

    @Builder
    public static class User {
        @JsonProperty
        private String username;
        @JsonProperty
        private RoleType[] roles;

        private static User from(UserEntity userEntity) {
            return User.builder()
                .username(userEntity.getUsername())
                .roles(userEntity.getRoles())
                .build();
        }
    }
}
