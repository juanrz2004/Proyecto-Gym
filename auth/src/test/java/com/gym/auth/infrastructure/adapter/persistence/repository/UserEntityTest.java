package com.gym.auth.infrastructure.adapter.persistence.entity;

import com.gym.auth.domain.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void userEntity_allConstructorAndGetters() {
        UserEntity e = new UserEntity(1L, "ignacio", "ignacio@gym.com", "hashed", Role.USER);
        assertEquals(1L, e.getId());
        assertEquals("ignacio", e.getUsername());
        assertEquals("ignacio@gym.com", e.getEmail());
        assertEquals("hashed", e.getPassword());
        assertEquals(Role.USER, e.getRole());
    }

    @Test
    void userEntity_defaultConstructorAndSetters() {
        UserEntity e = new UserEntity();
        e.setId(2L);
        e.setUsername("admin");
        e.setEmail("admin@gym.com");
        e.setPassword("enc");
        e.setRole(Role.ADMIN);
        assertEquals(2L, e.getId());
        assertEquals("admin", e.getUsername());
        assertEquals("admin@gym.com", e.getEmail());
        assertEquals("enc", e.getPassword());
        assertEquals(Role.ADMIN, e.getRole());
    }
}