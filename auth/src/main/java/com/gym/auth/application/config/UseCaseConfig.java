package com.gym.auth.application.config;

import com.gym.auth.application.usecase.LoginUserUseCase;
import com.gym.auth.application.usecase.RegisterUserUseCase;
import com.gym.auth.domain.port.input.LoginUserPort;
import com.gym.auth.domain.port.input.RegisterUserPort;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.TokenGeneratorPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterUserPort registerUserPort(UserRepositoryPort userRepositoryPort,
                                              PasswordEncoderPort passwordEncoderPort) {
        return new RegisterUserUseCase(userRepositoryPort, passwordEncoderPort);
    }

    @Bean
    public LoginUserPort loginUserPort(UserRepositoryPort userRepositoryPort,
                                       PasswordEncoderPort passwordEncoderPort,
                                       TokenGeneratorPort tokenGeneratorPort) {
        return new LoginUserUseCase(userRepositoryPort, passwordEncoderPort, tokenGeneratorPort);
    }
}
