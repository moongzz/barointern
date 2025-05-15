package com.moongoeun.project.user.application.service;

import com.moongoeun.project.user.application.dto.response.ResUserPatchAdminDTOApiV1;

public interface UserServiceApiV1 {
    ResUserPatchAdminDTOApiV1 adminBy(Long userId);
}
