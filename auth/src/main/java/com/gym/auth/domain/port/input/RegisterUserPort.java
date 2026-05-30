package com.gym.auth.domain.port.input;

import com.gym.auth.domain.model.User;

public interface RegisterUserPort {
    User register(String username, String email, String rawPassword, String role);
}
