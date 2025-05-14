package com.moongoeun.project.user.application.service;

import com.moongoeun.project.global.exception.CustomException;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1.User;
import com.moongoeun.project.user.application.dto.response.ResAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.repository.UserRepository;
import com.moongoeun.project.user.domain.vo.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplApiV1 implements AuthServiceApiV1 {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResAuthPostJoinDTOApiV1 joinBy(ReqAuthPostJoinDTOApiV1 dto) {
        User reqUser = dto.getUser();
        validateDuplicateUser(reqUser.getUsername(), reqUser.getNickname());

        UserEntity savedUser = saveUser(reqUser);
        return ResAuthPostJoinDTOApiV1.of(savedUser);
    }

    private void validateDuplicateUser(String username, String nickname) {
        userRepository.findByUsername(username)
            .ifPresent(userEntity -> {
                throw new CustomException(AuthExceptionCode.DUPLICATE_USERNAME);
            });
        userRepository.findByNickname(nickname)
            .ifPresent(userEntity -> {
                throw new CustomException(AuthExceptionCode.DUPLICATE_NICKNAME);
            });
    }

    private UserEntity saveUser(User reqUser) {
        UserEntity userEntity = UserEntity.builder()
            .username(reqUser.getUsername())
            .password(passwordEncoder.encode(reqUser.getPassword()))
            .nickname(reqUser.getNickname())
            .roles(new RoleType[]{RoleType.USER})
            .build();
        return userRepository.save(userEntity);
    }
}
