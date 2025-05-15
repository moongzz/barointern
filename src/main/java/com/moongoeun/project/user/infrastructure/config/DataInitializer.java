package com.moongoeun.project.user.infrastructure.config;

import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.repository.UserRepository;
import com.moongoeun.project.user.domain.vo.RoleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        UserEntity defaultAdmin = UserEntity.of(
            "admin",
            passwordEncoder.encode("adminPassword"),
            "admin",
            new RoleType[] {RoleType.ADMIN}
        );
        userRepository.save(defaultAdmin);
    }
}
