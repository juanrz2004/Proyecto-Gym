package com.gym.auth.application.config;

import com.gym.auth.domain.port.input.LoginUserPort;
import com.gym.auth.domain.port.input.RegisterUserPort;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.TokenGeneratorPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    private final UseCaseConfig config = new UseCaseConfig();

    @Test
    void registerUserPort_beanNotNull() {
        RegisterUserPort port = config.registerUserPort(
                mock(UserRepositoryPort.class),
                mock(PasswordEncoderPort.class));
        assertNotNull(port);
    }

    @Test
    void loginUserPort_beanNotNull() {
        LoginUserPort port = config.loginUserPort(
                mock(UserRepositoryPort.class),
                mock(PasswordEncoderPort.class),
                mock(TokenGeneratorPort.class));
        assertNotNull(port);
    }
}