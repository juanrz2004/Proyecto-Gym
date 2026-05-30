package com.gym.auth.infrastructure.entrypoint;

import com.gym.auth.application.dto.AuthResponseDTO;
import com.gym.auth.application.dto.LoginRequestDTO;
import com.gym.auth.application.dto.RegisterRequestDTO;
import com.gym.auth.application.dto.UserResponseDTO;
import com.gym.auth.domain.model.User;
import com.gym.auth.domain.port.input.LoginUserPort;
import com.gym.auth.domain.port.input.RegisterUserPort;
import com.gym.auth.infrastructure.adapter.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserPort registerUserPort;
    private final LoginUserPort loginUserPort;
    private final JwtService jwtService;

    public AuthController(RegisterUserPort registerUserPort,
                          LoginUserPort loginUserPort,
                          JwtService jwtService) {
        this.registerUserPort = registerUserPort;
        this.loginUserPort = loginUserPort;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        User user = registerUserPort.register(
                dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRole());
        UserResponseDTO response = new UserResponseDTO(
                user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = loginUserPort.login(dto.getUsername(), dto.getPassword());
        String role = jwtService.extractRole(token);
        AuthResponseDTO response = new AuthResponseDTO(token, dto.getUsername(), role);
        return ResponseEntity.ok(response);
    }
}
