package com.gym.auth.infrastructure.entrypoint;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidation_returns400WithFields() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of(
                new FieldError("obj", "username", "El username es obligatorio")));

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody().get("detalle"));
    }

    @Test
    void handleJson_returns400() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "bad json", new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Map<String, Object>> response = handler.handleJson(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("JSON mal formado", response.getBody().get("error"));
    }

    @Test
    void handleRuntime_yaEsta_returns400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleRuntime(new RuntimeException("El username ya está en uso"));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleRuntime_invalido_returns400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleRuntime(new RuntimeException("Rol inválido"));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleRuntime_mensajeGenerico_returns401() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleRuntime(new RuntimeException("Error de autenticacion"));

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void handleRuntime_credencialesInvalidas_returns400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleRuntime(new RuntimeException("Credenciales inválidas"));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleGeneral_returns500() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneral(new Exception("Error inesperado"));

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().get("error").toString().contains("Error interno"));
    }
}