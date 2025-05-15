package com.moongoeun.project.user.infrastructure.userdetails;

import com.moongoeun.project.global.exception.CustomException;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(AuthExceptionCode.USER_NOT_FOUND));
        Long id = userRepository.findIdByUserEntity(userEntity)
            .orElseThrow(() -> new CustomException(AuthExceptionCode.USER_NOT_FOUND));
        return new UserDetailsImpl(id, userEntity);
    }

    public UserDetails loadUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
            .orElseThrow(() -> new CustomException(AuthExceptionCode.USER_NOT_FOUND));

        return new UserDetailsImpl(id, userEntity);
    }
}
