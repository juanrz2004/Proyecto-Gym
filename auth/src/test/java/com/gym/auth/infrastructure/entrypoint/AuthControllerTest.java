package com.gym.auth.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.auth.application.dto.LoginRequestDTO;
import com.gym.auth.application.dto.RegisterRequestDTO;
import com.gym.auth.domain.model.Role;
import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.input.LoginUserPort;
import com.gym.auth.domain.port.input.RegisterUserPort;
import com.gym.auth.infrastructure.adapter.security.JwtAuthFilter;
import com.gym.auth.infrastructure.adapter.security.JwtService;
import com.gym.auth.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private RegisterUserPort registerUserPort;
    @MockBean private LoginUserPort loginUserPort;
    @MockBean private JwtService jwtService;

    @Test
    @DisplayName("POST /auth/register — crea usuario exitosamente")
    void register_success() throws Exception {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setUsername("ignacio");
        req.setEmail("ignacio@gym.com");
        req.setPassword("pass123");
        req.setRole("USER");

        User user = new User(1L, "ignacio", "ignacio@gym.com", "hashed", Role.USER);
        when(registerUserPort.register(any(), any(), any(), any())).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("ignacio"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("POST /auth/register — falla con datos inválidos")
    void register_invalidData_returns400() throws Exception {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setUsername("ab");          // muy corto
        req.setEmail("not-an-email");   // email inválido
        req.setPassword("123");         // muy corta

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /auth/login — retorna token JWT")
    void login_success() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setUsername("ignacio");
        req.setPassword("pass123");

        when(loginUserPort.login("ignacio", "pass123")).thenReturn("jwt.token.here");
        when(jwtService.extractRole("jwt.token.here")).thenReturn("USER");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value("ignacio"));
    }

    @Test
    @DisplayName("POST /auth/login — falla con credenciales inválidas")
    void login_invalidCredentials_returns401() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setUsername("ignacio");
        req.setPassword("wrong");

        when(loginUserPort.login("ignacio", "wrong"))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
