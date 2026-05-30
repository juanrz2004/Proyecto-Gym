package com.gym.auth.domain.port.output;

import com.gym.auth.domain.model.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}
