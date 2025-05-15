package com.moongoeun.project.user.domain.vo;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER);

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
