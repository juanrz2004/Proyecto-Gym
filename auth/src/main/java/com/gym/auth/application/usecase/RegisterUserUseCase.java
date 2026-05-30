package com.gym.auth.application.usecase;

import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.input.RegisterUserPort;
import com.gym.auth.domain.port.output.PasswordEncoderPort;
import com.gym.auth.domain.port.output.UserRepositoryPort;

public class RegisterUserUseCase implements RegisterUserPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegisterUserUseCase(UserRepositoryPort userRepositoryPort,
                                PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public User register(String username, String email, String rawPassword, String role) {
        if (userRepositoryPort.existsByUsername(username))
            throw new RuntimeException("El username '" + username + "' ya está en uso");
        if (userRepositoryPort.existsByEmail(email))
            throw new RuntimeException("El email '" + email + "' ya está registrado");

        Role userRole;
        try {
            userRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido. Usa: USER o ADMIN");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoderPort.encode(rawPassword));
        user.setRole(userRole);

        return userRepositoryPort.save(user);
    }
}
