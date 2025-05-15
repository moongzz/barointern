package com.moongoeun.project.user.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqAuthPostJoinDTOApiV1 {
    @Valid
    @NotNull(message = "회원 정보를 입력해주세요.")
    private User user;

    @Getter
    @Builder
    public static class User {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(max = 30, message = "아이디는 30자까지 가능합니다.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 10, max = 100, message = "비밀번호는 10자 이상 100자 이하까지 가능합니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(max = 30, message = "닉네임은 30자까지 가능합니다.")
        private String nickname;
    }
}
