package com.gym.auth.application.usecase;

import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.input.LoginUserPort;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.TokenGeneratorPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;

public class LoginUserUseCase implements LoginUserPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    public LoginUserUseCase(UserRepositoryPort userRepositoryPort,
                             PasswordEncoderPort passwordEncoderPort,
                             TokenGeneratorPort tokenGeneratorPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override
    public String login(String username, String rawPassword) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoderPort.matches(rawPassword, user.getPassword()))
            throw new RuntimeException("Credenciales inválidas");

        return tokenGeneratorPort.generateToken(user);
    }
}
