package com.moongoeun.project.user.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {
    CATEGORY("category"),
    USER_ID("userId"),
    ROLES("roles"),
    NICKNAME("nickname");

    private final String name;
}
