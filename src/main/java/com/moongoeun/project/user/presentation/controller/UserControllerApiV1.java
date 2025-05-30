package com.moongoeun.project.user.presentation.controller;

import com.moongoeun.project.global.response.ResDTO;
import com.moongoeun.project.user.application.dto.response.ResUserPatchAdminDTOApiV1;
import com.moongoeun.project.user.application.service.UserServiceApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerApiV1 {

    private final UserServiceApiV1 userService;

    @PatchMapping("/{id}/admin")
    public ResponseEntity<ResDTO<ResUserPatchAdminDTOApiV1>> adminBy(
        @PathVariable Long id
    ) {
        ResUserPatchAdminDTOApiV1 data = userService.adminBy(id);

        return new ResponseEntity<>(
            ResDTO.<ResUserPatchAdminDTOApiV1>builder()
                .code("0")
                .message("권한 수정 성공했습니다.")
                .data(data)
                .build(),
            HttpStatus.OK
        );
    }
}
