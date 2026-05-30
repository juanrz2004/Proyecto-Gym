package com.gym.auth.infrastructure.adapter.persistence.repository;

import com.gym.auth.infrastructure.adapter.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(null, "testuser", "test@email.com", "encodedPassword", "ROLE_USER");
        entityManager.persistAndFlush(userEntity);
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        Optional<UserEntity> result = userJpaRepository.findByUsername("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        assertThat(result.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        Optional<UserEntity> result = userJpaRepository.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_WhenEmailExists_ShouldReturnUser() {
        Optional<UserEntity> result = userJpaRepository.findByEmail("test@email.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        Optional<UserEntity> result = userJpaRepository.findByEmail("notfound@email.com");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByUsername_WhenUserExists_ShouldReturnTrue() {
        boolean exists = userJpaRepository.existsByUsername("testuser");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_WhenUserDoesNotExist_ShouldReturnFalse() {
        boolean exists = userJpaRepository.existsByUsername("ghost");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        boolean exists = userJpaRepository.existsByEmail("test@email.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        boolean exists = userJpaRepository.existsByEmail("missing@email.com");

        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistUserCorrectly() {
        UserEntity newUser = new UserEntity(null, "newuser", "new@email.com", "pass123", "ROLE_ADMIN");

        UserEntity saved = userJpaRepository.save(newUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getEmail()).isEqualTo("new@email.com");
        assertThat(saved.getRole()).isEqualTo("ROLE_ADMIN");
    }
}
