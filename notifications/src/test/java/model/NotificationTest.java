package com.gym.notifications.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    void builder_shouldCreateNotificationWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Notification n = Notification.builder()
                .id(1L).title("Title").message("Msg")
                .userId(5L).read(false).createdAt(now).build();

        assertThat(n.getId()).isEqualTo(1L);
        assertThat(n.getTitle()).isEqualTo("Title");
        assertThat(n.getMessage()).isEqualTo("Msg");
        assertThat(n.getUserId()).isEqualTo(5L);
        assertThat(n.isRead()).isFalse();
        assertThat(n.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void setters_shouldModifyFields() {
        Notification n = new Notification();
        n.setId(2L);
        n.setTitle("Updated");
        n.setMessage("New Msg");
        n.setUserId(10L);
        n.setRead(true);
        n.setCreatedAt(LocalDateTime.of(2024, 6, 1, 12, 0));

        assertThat(n.getId()).isEqualTo(2L);
        assertThat(n.getTitle()).isEqualTo("Updated");
        assertThat(n.isRead()).isTrue();
    }

    @Test
    void equalsAndHashCode_withSameId_shouldBeEqual() {
        Notification n1 = Notification.builder().id(1L).title("A").build();
        Notification n2 = Notification.builder().id(1L).title("A").build();

        assertThat(n1).isEqualTo(n2);
        assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
    }

    @Test
    void equalsAndHashCode_withDifferentId_shouldNotBeEqual() {
        Notification n1 = Notification.builder().id(1L).build();
        Notification n2 = Notification.builder().id(2L).build();

        assertThat(n1).isNotEqualTo(n2);
    }

    @Test
    void toString_shouldContainFieldValues() {
        Notification n = Notification.builder().id(1L).title("Gym").build();

        assertThat(n.toString()).contains("1");
        assertThat(n.toString()).contains("Gym");
    }

    @Test
    void defaultReadValue_shouldBeFalse() {
        Notification n = new Notification();
        assertThat(n.isRead()).isFalse();
    }
}

