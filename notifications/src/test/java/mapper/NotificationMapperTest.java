package com.gym.notifications.infraestructure.mapper;

import com.gym.notifications.application.dto.NotificationRequestDTO;
import com.gym.notifications.application.dto.NotificationResponseDTO;
import com.gym.notifications.application.dto.NotificationUpdateDTO;
import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.infraestructure.driver_adapters.jpa_repository.NotificationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationMapperTest {

    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
    }

    @Test
    void toNotificationFromDTO_mapsAllFields() {
        NotificationRequestDTO dto = new NotificationRequestDTO(
                "NOTIF-001", "CC12345678", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa."
        );

        Notification result = mapper.toNotificationFromDTO(dto);

        assertThat(result.getCode()).isEqualTo("NOTIF-001");
        assertThat(result.getRecipientDocument()).isEqualTo("CC12345678");
        assertThat(result.getRecipientPhone()).isEqualTo("+573001234567");
        assertThat(result.getType()).isEqualTo("WELCOME");
        assertThat(result.getSubject()).isEqualTo("Bienvenido");
        assertThat(result.getMessage()).isEqualTo("Tu membresía está activa.");
        assertThat(result.getStatus()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getSentAt()).isNull();
    }

    @Test
    void toNotificationFromUpdateDTO_mapsAllFields() {
        NotificationUpdateDTO dto = new NotificationUpdateDTO(
                "NOTIF-002", "CC99887766", "+573109876543",
                "PAYMENT_DUE", "Pago pendiente", "Tienes un saldo por pagar."
        );

        Notification result = mapper.toNotificationFromUpdateDTO(dto);

        assertThat(result.getCode()).isEqualTo("NOTIF-002");
        assertThat(result.getRecipientDocument()).isEqualTo("CC99887766");
        assertThat(result.getRecipientPhone()).isEqualTo("+573109876543");
        assertThat(result.getType()).isEqualTo("PAYMENT_DUE");
        assertThat(result.getSubject()).isEqualTo("Pago pendiente");
        assertThat(result.getMessage()).isEqualTo("Tienes un saldo por pagar.");
        assertThat(result.getStatus()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getSentAt()).isNull();
    }

    @Test
    void toNotificationResponseDTO_pendingNotification_mapsAllFields() {
        Notification n = new Notification(
                "NOTIF-003", "CC12345678", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                "PENDING", "2024-01-01 10:00:00", null
        );

        NotificationResponseDTO result = mapper.toNotificationResponseDTO(n);

        assertThat(result.getCode()).isEqualTo("NOTIF-003");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getCreatedAt()).isEqualTo("2024-01-01 10:00:00");
        assertThat(result.getSentAt()).isNull();
    }

    @Test
    void toNotificationResponseDTO_sentNotification_includesSentAt() {
        Notification n = new Notification(
                "NOTIF-004", "CC99887766", "+573109876543",
                "PAYMENT_DUE", "Pago", "Mensaje.",
                "SENT", "2024-02-01 08:00:00", "2024-02-01 08:03:00"
        );

        NotificationResponseDTO result = mapper.toNotificationResponseDTO(n);

        assertThat(result.getStatus()).isEqualTo("SENT");
        assertThat(result.getSentAt()).isEqualTo("2024-02-01 08:03:00");
    }

    @Test
    void toData_mapsNotificationToEntity() {
        Notification n = new Notification(
                "NOTIF-005", "CC55443322", "+573151234567",
                "MEMBERSHIP_EXPIRY", "Membresía vence", "Renueva pronto.",
                "PENDING", "2024-04-10 11:00:00", null
        );

        NotificationData result = mapper.toData(n);

        assertThat(result.getCode()).isEqualTo("NOTIF-005");
        assertThat(result.getType()).isEqualTo("MEMBERSHIP_EXPIRY");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getSentAt()).isNull();
    }

    @Test
    void toData_sentNotification_includesSentAt() {
        Notification n = new Notification(
                "NOTIF-006", "TI88776655", "+573201234567",
                "CANCELLATION", "Cancelación", "Membresía cancelada.",
                "SENT", "2024-05-01 07:00:00", "2024-05-01 07:01:30"
        );

        assertThat(mapper.toData(n).getSentAt()).isEqualTo("2024-05-01 07:01:30");
    }

    @Test
    void toDomain_mapsEntityToNotification() {
        NotificationData data = new NotificationData(
                "NOTIF-007", "CC12300456", "+573001234599",
                "WELCOME", "Bienvenido al gym", "Accede a nuestras instalaciones.",
                "PENDING", "2024-07-01 09:00:00", null
        );

        Notification result = mapper.toDomain(data);

        assertThat(result.getCode()).isEqualTo("NOTIF-007");
        assertThat(result.getType()).isEqualTo("WELCOME");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getSentAt()).isNull();
    }

    @Test
    void toDomain_sentData_includesSentAt() {
        NotificationData data = new NotificationData(
                "NOTIF-008", "CC99001122", "+573151001122",
                "PAYMENT_DUE", "Pago", "Realiza tu pago.",
                "SENT", "2024-08-01 10:00:00", "2024-08-01 10:02:45"
        );

        assertThat(mapper.toDomain(data).getSentAt()).isEqualTo("2024-08-01 10:02:45");
    }

    @Test
    void roundTrip_toDataAndToDomain_preservesAllFields() {
        Notification original = new Notification(
                "NOTIF-RT", "CC12345678", "+573001234567",
                "MEMBERSHIP_EXPIRY", "Vence pronto", "Renueva en 5 días.",
                "SENT", "2024-10-01 10:00:00", "2024-10-01 10:00:30"
        );

        Notification restored = mapper.toDomain(mapper.toData(original));

        assertThat(restored.getCode()).isEqualTo(original.getCode());
        assertThat(restored.getStatus()).isEqualTo(original.getStatus());
        assertThat(restored.getSentAt()).isEqualTo(original.getSentAt());
    }
}