package com.gym.notifications.application.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationResponseDTOTest {

    @Test
    void constructor_allFields_setsCorrectly() {
        NotificationResponseDTO dto = new NotificationResponseDTO(
                "NOTIF-001", "CC12345678", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                "SENT", "2024-01-01 10:00:00", "2024-01-01 10:01:00"
        );

        assertThat(dto.getCode()).isEqualTo("NOTIF-001");
        assertThat(dto.getRecipientDocument()).isEqualTo("CC12345678");
        assertThat(dto.getRecipientPhone()).isEqualTo("+573001234567");
        assertThat(dto.getType()).isEqualTo("WELCOME");
        assertThat(dto.getSubject()).isEqualTo("Bienvenido");
        assertThat(dto.getMessage()).isEqualTo("Tu membresía está activa.");
        assertThat(dto.getStatus()).isEqualTo("SENT");
        assertThat(dto.getCreatedAt()).isEqualTo("2024-01-01 10:00:00");
        assertThat(dto.getSentAt()).isEqualTo("2024-01-01 10:01:00");
    }

    @Test
    void defaultConstructor_fieldsAreNull() {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        assertThat(dto.getCode()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getSentAt()).isNull();
    }

    @Test
    void setters_updateAllFields() {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setCode("NOTIF-002");
        dto.setRecipientDocument("CC99887766");
        dto.setRecipientPhone("+573109876543");
        dto.setType("PAYMENT_DUE");
        dto.setSubject("Pago pendiente");
        dto.setMessage("Tienes un saldo por pagar.");
        dto.setStatus("PENDING");
        dto.setCreatedAt("2024-06-01 08:00:00");
        dto.setSentAt(null);

        assertThat(dto.getCode()).isEqualTo("NOTIF-002");
        assertThat(dto.getRecipientDocument()).isEqualTo("CC99887766");
        assertThat(dto.getRecipientPhone()).isEqualTo("+573109876543");
        assertThat(dto.getType()).isEqualTo("PAYMENT_DUE");
        assertThat(dto.getSubject()).isEqualTo("Pago pendiente");
        assertThat(dto.getMessage()).isEqualTo("Tienes un saldo por pagar.");
        assertThat(dto.getStatus()).isEqualTo("PENDING");
        assertThat(dto.getCreatedAt()).isEqualTo("2024-06-01 08:00:00");
        assertThat(dto.getSentAt()).isNull();
    }
}