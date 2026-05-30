package com.gym.auth.application.usecase;

import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.TokenGeneratorPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordEncoderPort passwordEncoderPort;
    @Mock private TokenGeneratorPort tokenGeneratorPort;

    private LoginUserUseCase useCase;
    private User user;

    @BeforeEach
    void setUp() {
        useCase = new LoginUserUseCase(userRepositoryPort, passwordEncoderPort, tokenGeneratorPort);
        user = new User(1L, "ignacio", "ignacio@gym.com", "$2a$hashed", Role.USER);
    }

    @Test
    @DisplayName("Login exitoso retorna token JWT")
    void login_success_returnsToken() {
        when(userRepositoryPort.findByUsername("ignacio")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("pass123", "$2a$hashed")).thenReturn(true);
        when(tokenGeneratorPort.generateToken(user)).thenReturn("jwt.token.here");

        String token = useCase.login("ignacio", "pass123");

        assertEquals("jwt.token.here", token);
        verify(tokenGeneratorPort).generateToken(user);
    }

    @Test
    @DisplayName("Error: usuario no encontrado")
    void login_userNotFound_throwsException() {
        when(userRepositoryPort.findByUsername("noexiste")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.login("noexiste", "pass"));
        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(tokenGeneratorPort, never()).generateToken(any());
    }

    @Test
    @DisplayName("Error: contraseña incorrecta")
    void login_wrongPassword_throwsException() {
        when(userRepositoryPort.findByUsername("ignacio")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("wrong", "$2a$hashed")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.login("ignacio", "wrong"));
        assertEquals("Credenciales inválidas", ex.getMessage());
    }
}
