package com.moongoeun.project.user.domain.entity;

import com.moongoeun.project.user.domain.vo.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {
    private String username;
    private String password;
    private String nickname;
    private RoleType[] roles;

    @Builder
    private UserEntity(
        String username,
        String password,
        String nickname,
        RoleType[] roles
    ) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static UserEntity of(
        String username,
        String password,
        String nickname,
        RoleType[] roles
    ) {
        return UserEntity.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .roles(roles)
            .build();
    }
}
