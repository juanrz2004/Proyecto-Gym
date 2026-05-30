
package com.gym.notifications.infraestructure.driver_adapters.jpa_repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDataTest {

    // ─── Constructor completo ────────────────────────────────────────────────

    @Test
    void fullConstructor_setsAllFields() {
        NotificationData data = new NotificationData(
                "NOTIF-001", "CC123456", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                "PENDING", "2024-01-01 10:00:00", null);

        assertEquals("NOTIF-001", data.getCode());
        assertEquals("CC123456", data.getRecipientDocument());
        assertEquals("+573001234567", data.getRecipientPhone());
        assertEquals("WELCOME", data.getType());
        assertEquals("Bienvenido", data.getSubject());
        assertEquals("Tu membresía está activa.", data.getMessage());
        assertEquals("PENDING", data.getStatus());
        assertEquals("2024-01-01 10:00:00", data.getCreatedAt());
        assertNull(data.getSentAt());
    }

    // ─── Constructor vacío + setters ────────────────────────────────────────

    @Test
    void defaultConstructorAndSetters_workCorrectly() {
        NotificationData data = new NotificationData();

        data.setCode("NOTIF-002");
        data.setRecipientDocument("CC999999");
        data.setRecipientPhone("+573009999999");
        data.setType("PAYMENT_DUE");
        data.setSubject("Pago pendiente");
        data.setMessage("Tienes un pago pendiente.");
        data.setStatus("SENT");
        data.setCreatedAt("2024-02-01 08:00:00");
        data.setSentAt("2024-02-01 09:00:00");

        assertEquals("NOTIF-002", data.getCode());
        assertEquals("CC999999", data.getRecipientDocument());
        assertEquals("+573009999999", data.getRecipientPhone());
        assertEquals("PAYMENT_DUE", data.getType());
        assertEquals("Pago pendiente", data.getSubject());
        assertEquals("Tienes un pago pendiente.", data.getMessage());
        assertEquals("SENT", data.getStatus());
        assertEquals("2024-02-01 08:00:00", data.getCreatedAt());
        assertEquals("2024-02-01 09:00:00", data.getSentAt());
    }

    // ─── Ciclo de vida de status ─────────────────────────────────────────────

    @Test
    void statusTransitions_fromPendingToSentToRead() {
        NotificationData data = new NotificationData();
        data.setStatus("PENDING");
        assertEquals("PENDING", data.getStatus());

        data.setStatus("SENT");
        data.setSentAt("2024-01-01 11:00:00");
        assertEquals("SENT", data.getStatus());
        assertNotNull(data.getSentAt());

        data.setStatus("READ");
        assertEquals("READ", data.getStatus());
    }

    @Test
    void statusFailed_canBeSet() {
        NotificationData data = new NotificationData();
        data.setStatus("FAILED");
        assertEquals("FAILED", data.getStatus());
    }

    // ─── Tipos válidos ───────────────────────────────────────────────────────

    @Test
    void allNotificationTypes_canBeSet() {
        NotificationData data = new NotificationData();

        for (String type : new String[]{"MEMBERSHIP_EXPIRY", "PAYMENT_DUE", "WELCOME", "CANCELLATION", "CUSTOM"}) {
            data.setType(type);
            assertEquals(type, data.getType());
        }
    }

    // ─── Mensaje largo ───────────────────────────────────────────────────────

    @Test
    void longMessage_canBeStored() {
        NotificationData data = new NotificationData();
        String longMessage = "A".repeat(2000);
        data.setMessage(longMessage);
        assertEquals(2000, data.getMessage().length());
    }
}
