package com.gym.auth.infrastructure.adapter.persistence.repository;

import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.output.UserRepositoryPort;
import com.gym.auth.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
    }

    private User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getEmail(),
                entity.getPassword(), entity.getRole());
    }
}
