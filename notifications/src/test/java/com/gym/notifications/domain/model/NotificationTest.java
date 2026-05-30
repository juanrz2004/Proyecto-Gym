package com.gym.notifications.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    void builder_shouldCreateNotificationWithAllFields() {
        Notification n = new Notification(
                "NOTIF-001",
                "CC12345678",
                "+573001234567",
                "WELCOME",
                "Title",
                "Msg",
                "PENDING",
                "2024-01-01 10:00:00",
                null
        );

        assertThat(n.getCode()).isEqualTo("NOTIF-001");
        assertThat(n.getRecipientDocument()).isEqualTo("CC12345678");
        assertThat(n.getRecipientPhone()).isEqualTo("+573001234567");
        assertThat(n.getType()).isEqualTo("WELCOME");
        assertThat(n.getSubject()).isEqualTo("Title");
        assertThat(n.getMessage()).isEqualTo("Msg");
        assertThat(n.getStatus()).isEqualTo("PENDING");
        assertThat(n.getCreatedAt()).isEqualTo("2024-01-01 10:00:00");
        assertThat(n.getSentAt()).isNull();
    }

    @Test
    void setters_shouldModifyFields() {
        Notification n = new Notification();
        n.setCode("NOTIF-002");
        n.setRecipientDocument("CC99887766");
        n.setRecipientPhone("+573009876543");
        n.setType("PAYMENT_DUE");
        n.setSubject("Updated");
        n.setMessage("New Msg");
        n.setStatus("SENT");
        n.setCreatedAt("2024-06-01 12:00:00");
        n.setSentAt("2024-06-01 12:01:00");

        assertThat(n.getCode()).isEqualTo("NOTIF-002");
        assertThat(n.getSubject()).isEqualTo("Updated");
        assertThat(n.getMessage()).isEqualTo("New Msg");
        assertThat(n.getStatus()).isEqualTo("SENT");
        assertThat(n.getSentAt()).isEqualTo("2024-06-01 12:01:00");
    }

    @Test
    void equalsAndHashCode_withSameId_shouldBeEqual() {
        Notification n1 = new Notification(
                "NOTIF-003", "CC11111111", "+573001111111",
                "WELCOME", "Title", "Msg", "PENDING",
                "2024-01-01 10:00:00", null);
        Notification n2 = new Notification(
                "NOTIF-003", "CC11111111", "+573001111111",
                "WELCOME", "Title", "Msg", "PENDING",
                "2024-01-01 10:00:00", null);

        assertThat(n1.getCode()).isEqualTo(n2.getCode());
        assertThat(n1.getSubject()).isEqualTo(n2.getSubject());
        assertThat(n1.getMessage()).isEqualTo(n2.getMessage());
        assertThat(n1.getStatus()).isEqualTo(n2.getStatus());
    }

    @Test
    void equalsAndHashCode_withDifferentId_shouldNotBeEqual() {
        Notification n1 = new Notification();
        n1.setCode("NOTIF-AAA");

        Notification n2 = new Notification();
        n2.setCode("NOTIF-BBB");

        assertThat(n1.getCode()).isNotEqualTo(n2.getCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        Notification n = new Notification();
        n.setCode("NOTIF-GYM");
        n.setSubject("Gym");

        assertThat(n.getCode()).contains("NOTIF-GYM");
        assertThat(n.getSubject()).contains("Gym");
    }

    @Test
    void defaultReadValue_shouldBeFalse() {
        Notification n = new Notification();
        assertThat(n.getStatus()).isNull();
    }
}