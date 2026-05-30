package com.gym.auth.domain.port.input;

public interface LoginUserPort {
    String login(String username, String rawPassword);
}
