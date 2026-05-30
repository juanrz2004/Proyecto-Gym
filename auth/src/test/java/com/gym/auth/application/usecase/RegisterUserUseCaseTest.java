package com.gym.auth.application.usecase;

import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordEncoderPort passwordEncoderPort;

    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCase(userRepositoryPort, passwordEncoderPort);
    }

    @Test
    @DisplayName("Registro exitoso con rol USER")
    void register_success_roleUser() {
        when(userRepositoryPort.existsByUsername("ignacio")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("ignacio@gym.com")).thenReturn(false);
        when(passwordEncoderPort.encode("pass123")).thenReturn("$2a$hashed");
        when(userRepositoryPort.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User result = useCase.register("ignacio", "ignacio@gym.com", "pass123", "USER");

        assertNotNull(result);
        assertEquals("ignacio", result.getUsername());
        assertEquals(Role.USER, result.getRole());
        assertEquals("$2a$hashed", result.getPassword());
        verify(userRepositoryPort).save(any());
    }

    @Test
    @DisplayName("Registro exitoso con rol ADMIN")
    void register_success_roleAdmin() {
        when(userRepositoryPort.existsByUsername("admin")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("admin@gym.com")).thenReturn(false);
        when(passwordEncoderPort.encode(any())).thenReturn("hashed");
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.register("admin", "admin@gym.com", "admin123", "ADMIN");

        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    @DisplayName("Error: username duplicado")
    void register_duplicateUsername_throwsException() {
        when(userRepositoryPort.existsByUsername("ignacio")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.register("ignacio", "otro@gym.com", "pass", "USER"));
        assertTrue(ex.getMessage().contains("username"));
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Error: email duplicado")
    void register_duplicateEmail_throwsException() {
        when(userRepositoryPort.existsByUsername("nuevo")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("ignacio@gym.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.register("nuevo", "ignacio@gym.com", "pass", "USER"));
        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    @DisplayName("Error: rol inválido")
    void register_invalidRole_throwsException() {
        when(userRepositoryPort.existsByUsername("ignacio")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("ignacio@gym.com")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.register("ignacio", "ignacio@gym.com", "pass", "SUPERADMIN"));
        assertTrue(ex.getMessage().contains("Rol inválido"));
    }
}
