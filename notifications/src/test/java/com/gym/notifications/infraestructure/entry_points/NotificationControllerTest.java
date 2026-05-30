package com.gym.notifications.infraestructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.notifications.application.dto.NotificationRequestDTO;
import com.gym.notifications.application.dto.NotificationResponseDTO;
import com.gym.notifications.application.dto.NotificationUpdateDTO;
import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.domain.usecase.NotificationUseCase;
import com.gym.notifications.infraestructure.mapper.NotificationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationUseCase useCase;

    @MockBean
    private NotificationMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Notification buildNotification(String code, String status) {
        return new Notification(code, "CC123456", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                status, "2024-01-01 10:00:00", null);
    }

    private NotificationResponseDTO buildResponseDTO(String code, String status) {
        return new NotificationResponseDTO(code, "CC123456", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                status, "2024-01-01 10:00:00", null);
    }

    // ─── POST /save ──────────────────────────────────────────────────────────

    @Test
    void saveNotification_validRequest_returns201() throws Exception {
        NotificationRequestDTO dto = new NotificationRequestDTO(
                "NOTIF-001", "CC123456", "+573001234567", "WELCOME", "Bienvenido", "Tu membresía está activa.");
        Notification saved = buildNotification("NOTIF-001", "PENDING");

        when(mapper.toNotificationFromDTO(any())).thenReturn(saved);
        when(useCase.saveNotification(any())).thenReturn(saved);
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("NOTIF-001", "PENDING"));

        mockMvc.perform(post("/api/gym/notification/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("NOTIF-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void saveNotification_duplicateCode_returns409() throws Exception {
        NotificationRequestDTO dto = new NotificationRequestDTO(
                "NOTIF-001", "CC123456", "+573001234567", "WELCOME", "Bienvenido", "Mensaje");
        Notification n = buildNotification("NOTIF-001", null);

        when(mapper.toNotificationFromDTO(any())).thenReturn(n);
        when(useCase.saveNotification(any())).thenThrow(new IllegalArgumentException("Ya existe"));

        mockMvc.perform(post("/api/gym/notification/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    // ─── GET /get/{code} ─────────────────────────────────────────────────────

    @Test
    void getNotification_existingCode_returns200() throws Exception {
        Notification n = buildNotification("NOTIF-001", "PENDING");
        when(useCase.getNotificationByCode("NOTIF-001")).thenReturn(n);
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("NOTIF-001", "PENDING"));

        mockMvc.perform(get("/api/gym/notification/get/NOTIF-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOTIF-001"));
    }

    @Test
    void getNotification_missingCode_returns404() throws Exception {
        when(useCase.getNotificationByCode("XXX")).thenThrow(new NoSuchElementException("No existe"));

        mockMvc.perform(get("/api/gym/notification/get/XXX"))
                .andExpect(status().isNotFound());
    }

    // ─── GET /list ───────────────────────────────────────────────────────────

    @Test
    void listNotifications_returns200() throws Exception {
        when(useCase.listNotifications()).thenReturn(List.of());

        mockMvc.perform(get("/api/gym/notification/list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ─── PUT /update/{code} ──────────────────────────────────────────────────

    @Test
    void updateNotification_valid_returns200() throws Exception {
        NotificationUpdateDTO dto = new NotificationUpdateDTO(
                "NOTIF-001", "CC123456", "+573001234567", "CUSTOM", "Nuevo asunto", "Nuevo mensaje");
        Notification updated = buildNotification("NOTIF-001", "PENDING");

        when(mapper.toNotificationFromUpdateDTO(any())).thenReturn(updated);
        when(useCase.updateNotification(eq("NOTIF-001"), any())).thenReturn(updated);
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("NOTIF-001", "PENDING"));

        mockMvc.perform(put("/api/gym/notification/update/NOTIF-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateNotification_notPending_returns400() throws Exception {
        NotificationUpdateDTO dto = new NotificationUpdateDTO(
                "NOTIF-001", "CC123456", "+573001234567", "CUSTOM", "Asunto", "Mensaje");
        Notification n = buildNotification("NOTIF-001", null);

        when(mapper.toNotificationFromUpdateDTO(any())).thenReturn(n);
        when(useCase.updateNotification(eq("NOTIF-001"), any()))
                .thenThrow(new IllegalStateException("Solo se pueden editar PENDING"));

        mockMvc.perform(put("/api/gym/notification/update/NOTIF-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ─── DELETE /delete/{code} ───────────────────────────────────────────────

    @Test
    void deleteNotification_existingCode_returns200() throws Exception {
        mockMvc.perform(delete("/api/gym/notification/delete/NOTIF-001"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNotification_missingCode_returns404() throws Exception {
        org.mockito.Mockito.doThrow(new NoSuchElementException("No existe"))
                .when(useCase).deleteNotificationByCode("XXX");

        mockMvc.perform(delete("/api/gym/notification/delete/XXX"))
                .andExpect(status().isNotFound());
    }

    // ─── PATCH /send/{code} — Twilio SMS ─────────────────────────────────────

    @Test
    void sendNotification_pending_returns200WithSentStatus() throws Exception {
        Notification sent = buildNotification("NOTIF-001", "SENT");
        sent.setSentAt("2024-01-01 11:00:00");
        when(useCase.sendNotification("NOTIF-001")).thenReturn(sent);
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("NOTIF-001", "SENT"));

        mockMvc.perform(patch("/api/gym/notification/send/NOTIF-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    void sendNotification_twilioFails_returns400() throws Exception {
        when(useCase.sendNotification("NOTIF-001"))
                .thenThrow(new IllegalStateException("Error al enviar el SMS vía Twilio: connection refused"));

        mockMvc.perform(patch("/api/gym/notification/send/NOTIF-001"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNotification_notFound_returns404() throws Exception {
        when(useCase.sendNotification("XXX")).thenThrow(new NoSuchElementException("No existe"));

        mockMvc.perform(patch("/api/gym/notification/send/XXX"))
                .andExpect(status().isNotFound());
    }

    // ─── PATCH /read/{code} ──────────────────────────────────────────────────

    @Test
    void markAsRead_sent_returns200() throws Exception {
        Notification read = buildNotification("NOTIF-001", "READ");
        when(useCase.markAsRead("NOTIF-001")).thenReturn(read);
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("NOTIF-001", "READ"));

        mockMvc.perform(patch("/api/gym/notification/read/NOTIF-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READ"));
    }

    @Test
    void markAsRead_notSent_returns400() throws Exception {
        when(useCase.markAsRead("NOTIF-001"))
                .thenThrow(new IllegalStateException("Solo SENT puede marcarse como leída"));

        mockMvc.perform(patch("/api/gym/notification/read/NOTIF-001"))
                .andExpect(status().isBadRequest());
    }

    // ─── GET /recipient/{doc} ────────────────────────────────────────────────

    @Test
    void listByRecipient_returns200() throws Exception {
        when(useCase.listByRecipient("CC123456")).thenReturn(List.of(
                buildNotification("N1", "SENT")));
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("N1", "SENT"));

        mockMvc.perform(get("/api/gym/notification/recipient/CC123456"))
                .andExpect(status().isOk());
    }

    // ─── GET /status/{status} ────────────────────────────────────────────────

    @Test
    void listByStatus_returns200() throws Exception {
        when(useCase.listByStatus("PENDING")).thenReturn(List.of(
                buildNotification("N1", "PENDING")));
        when(mapper.toNotificationResponseDTO(any())).thenReturn(buildResponseDTO("N1", "PENDING"));

        mockMvc.perform(get("/api/gym/notification/status/PENDING"))
                .andExpect(status().isOk());
    }
}
