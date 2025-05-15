package com.moongoeun.project.user.application.service;

import com.moongoeun.project.global.exception.CustomException;
import com.moongoeun.project.user.application.dto.response.ResUserPatchAdminDTOApiV1;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplApiV1 implements UserServiceApiV1 {

    private final UserRepository userRepository;

    @Override
    public ResUserPatchAdminDTOApiV1 adminBy(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(AuthExceptionCode.USER_NOT_FOUND));

        UserEntity updateUser = userEntity.updateRole();
        userRepository.update(userId, updateUser);

        return ResUserPatchAdminDTOApiV1.of(updateUser);
    }
}
