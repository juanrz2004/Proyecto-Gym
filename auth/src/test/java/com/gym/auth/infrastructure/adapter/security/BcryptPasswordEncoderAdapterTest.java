package com.gym.auth.infrastructure.adapter.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BcryptPasswordEncoderAdapterTest {

    private final BcryptPasswordEncoderAdapter adapter = new BcryptPasswordEncoderAdapter();

    @Test
    void encode_returnsNonNullHash() {
        String hash = adapter.encode("pass123");
        assertNotNull(hash);
        assertNotEquals("pass123", hash);
    }

    @Test
    void matches_correctPassword_returnsTrue() {
        String hash = adapter.encode("pass123");
        assertTrue(adapter.matches("pass123", hash));
    }

    @Test
    void matches_wrongPassword_returnsFalse() {
        String hash = adapter.encode("pass123");
        assertFalse(adapter.matches("wrongpass", hash));
    }
}