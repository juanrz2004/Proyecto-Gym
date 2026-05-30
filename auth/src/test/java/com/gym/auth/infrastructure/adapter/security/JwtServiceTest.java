package com.gym.auth.infrastructure.adapter.security;

import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    private static final String SECRET = "myTestSecretKeyForJwtThatIsAtLeast256BitsLongForSecurity!!";
    private static final long EXPIRATION = 3600000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);

        testUser = new User(1L, "testuser", "test@email.com", "encodedPass", Role.USER);
    }

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtService.generateToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    void generateToken_ShouldContainThreeParts() {
        String token = jwtService.generateToken(testUser);

        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken(testUser);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void extractRole_ShouldReturnCorrectRole() {
        String token = jwtService.generateToken(testUser);

        String role = jwtService.extractRole(token);

        assertThat(role).isEqualTo("USER");
    }

    @Test
    void extractRole_AdminUser_ShouldReturnAdminRole() {
        User adminUser = new User(2L, "adminuser", "admin@email.com", "pass", Role.ADMIN);
        String token = jwtService.generateToken(adminUser);

        String role = jwtService.extractRole(token);

        assertThat(role).isEqualTo("ADMIN");
    }

    @Test
    void isTokenValid_WithValidTokenAndCorrectUsername_ShouldReturnTrue() {
        String token = jwtService.generateToken(testUser);

        boolean isValid = jwtService.isTokenValid(token, "testuser");

        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_WithValidTokenButWrongUsername_ShouldReturnFalse() {
        String token = jwtService.generateToken(testUser);

        boolean isValid = jwtService.isTokenValid(token, "wronguser");

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_WithExpiredToken_ShouldReturnFalse() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);
        String expiredToken = jwtService.generateToken(testUser);

        boolean isValid = jwtService.isTokenValid(expiredToken, "testuser");

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_WithMalformedToken_ShouldReturnFalse() {
        boolean isValid = jwtService.isTokenValid("this.is.not.a.valid.jwt", "testuser");

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_WithEmptyToken_ShouldReturnFalse() {
        boolean isValid = jwtService.isTokenValid("", "testuser");

        assertThat(isValid).isFalse();
    }

    @Test
    void generateToken_DifferentUsers_ShouldReturnDifferentTokens() {
        User anotherUser = new User(2L, "otheruser", "other@email.com", "pass", Role.USER);

        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(anotherUser);

        assertThat(token1).isNotEqualTo(token2);
    }
}
