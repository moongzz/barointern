package com.moongoeun.project.user.application.service;

import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.dto.response.ResAuthPostJoinDTOApiV1;

public interface AuthServiceApiV1 {
    ResAuthPostJoinDTOApiV1 joinBy(ReqAuthPostJoinDTOApiV1 dto);
}
