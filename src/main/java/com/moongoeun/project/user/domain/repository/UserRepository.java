package com.moongoeun.project.user.domain.repository;

import com.moongoeun.project.user.domain.entity.UserEntity;
import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByNickname(String nickname);

    Optional<Long> findIdByUserEntity(UserEntity userEntity);

    Optional<UserEntity> findById(Long id);
}
