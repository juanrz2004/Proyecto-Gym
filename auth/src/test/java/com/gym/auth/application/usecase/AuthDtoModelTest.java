package com.gym.auth.application.usecase;

import com.gym.auth.application.dto.AuthResponseDTO;
import com.gym.auth.application.dto.LoginRequestDTO;
import com.gym.auth.application.dto.RegisterRequestDTO;
import com.gym.auth.application.dto.UserResponseDTO;
import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoModelTest {

    // ─── AuthResponseDTO ─────────────────────────────────────────────────────

    @Test
    void authResponseDTO_allConstructorAndGetters() {
        AuthResponseDTO dto = new AuthResponseDTO("token123", "ignacio", "USER");
        assertEquals("token123", dto.getToken());
        assertEquals("ignacio", dto.getUsername());
        assertEquals("USER", dto.getRole());
        assertEquals("Bearer", dto.getType());
    }

    @Test
    void authResponseDTO_defaultConstructorAndSetters() {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setToken("tok");
        dto.setUsername("user");
        dto.setRole("ADMIN");
        assertEquals("tok", dto.getToken());
        assertEquals("user", dto.getUsername());
        assertEquals("ADMIN", dto.getRole());
        assertEquals("Bearer", dto.getType());
    }

    // ─── UserResponseDTO ─────────────────────────────────────────────────────

    @Test
    void userResponseDTO_allConstructorAndGetters() {
        UserResponseDTO dto = new UserResponseDTO(1L, "ignacio", "ignacio@gym.com", "USER");
        assertEquals(1L, dto.getId());
        assertEquals("ignacio", dto.getUsername());
        assertEquals("ignacio@gym.com", dto.getEmail());
        assertEquals("USER", dto.getRole());
    }

    @Test
    void userResponseDTO_defaultConstructorAndSetters() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(2L);
        dto.setUsername("u");
        dto.setEmail("u@gym.com");
        dto.setRole("ADMIN");
        assertEquals(2L, dto.getId());
        assertEquals("u", dto.getUsername());
        assertEquals("u@gym.com", dto.getEmail());
        assertEquals("ADMIN", dto.getRole());
    }

    // ─── LoginRequestDTO ─────────────────────────────────────────────────────

    @Test
    void loginRequestDTO_settersAndGetters() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("ignacio");
        dto.setPassword("pass123");
        assertEquals("ignacio", dto.getUsername());
        assertEquals("pass123", dto.getPassword());
    }

    // ─── RegisterRequestDTO ──────────────────────────────────────────────────

    @Test
    void registerRequestDTO_defaultRoleIsUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        assertEquals("USER", dto.getRole());
    }

    @Test
    void registerRequestDTO_settersAndGetters() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("ign");
        dto.setEmail("ign@gym.com");
        dto.setPassword("pass123");
        dto.setRole("ADMIN");
        assertEquals("ign", dto.getUsername());
        assertEquals("ign@gym.com", dto.getEmail());
        assertEquals("pass123", dto.getPassword());
        assertEquals("ADMIN", dto.getRole());
    }

    // ─── User domain model ───────────────────────────────────────────────────

    @Test
    void user_defaultConstructorAndSetters() {
        User u = new User();
        u.setId(1L);
        u.setUsername("ignacio");
        u.setEmail("ignacio@gym.com");
        u.setPassword("hashed");
        u.setRole(Role.USER);
        assertEquals(1L, u.getId());
        assertEquals("ignacio", u.getUsername());
        assertEquals("ignacio@gym.com", u.getEmail());
        assertEquals("hashed", u.getPassword());
        assertEquals(Role.USER, u.getRole());
    }

    @Test
    void role_enumValues() {
        assertEquals(Role.USER, Role.valueOf("USER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }
}