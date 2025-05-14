package com.moongoeun.project.user.domain.repository;

import com.moongoeun.project.user.domain.entity.UserEntity;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Map<Long, UserEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public UserEntity save(UserEntity userEntity) {
        Long id = sequence.getAndIncrement();
        store.put(id, userEntity);
        return store.get(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return store.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    }

    @Override
    public Optional<UserEntity> findByNickname(String nickname) {
        return store.values().stream()
            .filter(user -> user.getNickname().equals(nickname))
            .findFirst();
    }

    @Override
    public Optional<Long> findIdByUserEntity(UserEntity userEntity) {
        return store.entrySet().stream()
            .filter(entry -> entry.getValue().equals(userEntity))
            .map(Map.Entry::getKey)
            .findFirst();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
