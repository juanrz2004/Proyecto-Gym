package com.gym.notifications.domain.usecase;

import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.domain.model.gateway.NotificationGateway;
import com.gym.notifications.domain.model.gateway.SmsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationUseCaseTest {

    @Mock
    private NotificationGateway gateway;

    @Mock
    private SmsGateway smsGateway;

    private NotificationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new NotificationUseCase(gateway, smsGateway);
    }

    private Notification buildNotification(String code, String status) {
        return new Notification(code, "CC123456", "+573001234567",
                "WELCOME", "Bienvenido al gimnasio", "Tu membresía está activa.",
                status, "2024-01-01 10:00:00", null);
    }

    // ─── saveNotification ────────────────────────────────────────────────────

    @Test
    void saveNotification_newCode_savesWithPendingStatus() {
        Notification input = buildNotification("NOTIF-001", null);
        when(gateway.existsByCode("NOTIF-001")).thenReturn(false);
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Notification result = useCase.saveNotification(input);

        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(gateway).save(any());
    }

    @Test
    void saveNotification_duplicateCode_throwsIllegalArgument() {
        when(gateway.existsByCode("NOTIF-001")).thenReturn(true);
        Notification input = buildNotification("NOTIF-001", null);

        assertThrows(IllegalArgumentException.class, () -> useCase.saveNotification(input));
        verify(gateway, never()).save(any());
    }

    // ─── getNotificationByCode ───────────────────────────────────────────────

    @Test
    void getNotificationByCode_existingCode_returnsNotification() {
        Notification notification = buildNotification("NOTIF-001", "PENDING");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));

        Notification result = useCase.getNotificationByCode("NOTIF-001");

        assertEquals("NOTIF-001", result.getCode());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void getNotificationByCode_missingCode_throwsNoSuchElement() {
        when(gateway.findByCode("NOTIF-XXX")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> useCase.getNotificationByCode("NOTIF-XXX"));
    }

    // ─── sendNotification (Twilio) ───────────────────────────────────────────

    @Test
    void sendNotification_pending_callsTwilioAndTransitionsToSent() {
        Notification notification = buildNotification("NOTIF-001", "PENDING");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(smsGateway.sendSms(anyString(), anyString())).thenReturn("SM_TEST_SID_001");

        Notification result = useCase.sendNotification("NOTIF-001");

        assertEquals("SENT", result.getStatus());
        assertNotNull(result.getSentAt());
        verify(smsGateway).sendSms(eq("+573001234567"), contains("Bienvenido al gimnasio"));
        verify(gateway).save(any());
    }

    @Test
    void sendNotification_twilioFails_transitionsToFailedAndThrows() {
        Notification notification = buildNotification("NOTIF-001", "PENDING");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(smsGateway.sendSms(anyString(), anyString()))
                .thenThrow(new RuntimeException("Twilio connection refused"));

        assertThrows(IllegalStateException.class, () -> useCase.sendNotification("NOTIF-001"));
        verify(gateway).save(argThat(n -> "FAILED".equals(n.getStatus())));
    }

    @Test
    void sendNotification_alreadySent_throwsIllegalState() {
        Notification notification = buildNotification("NOTIF-001", "SENT");
        notification.setSentAt("2024-01-01 11:00:00");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));

        assertThrows(IllegalStateException.class, () -> useCase.sendNotification("NOTIF-001"));
        verifyNoInteractions(smsGateway);
    }

    @Test
    void sendNotification_failed_throwsIllegalState() {
        Notification notification = buildNotification("NOTIF-001", "FAILED");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));

        assertThrows(IllegalStateException.class, () -> useCase.sendNotification("NOTIF-001"));
        verifyNoInteractions(smsGateway);
    }

    @Test
    void sendNotification_notFound_throwsNoSuchElement() {
        when(gateway.findByCode("NOTIF-XXX")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> useCase.sendNotification("NOTIF-XXX"));
        verifyNoInteractions(smsGateway);
    }

    // ─── markAsRead ──────────────────────────────────────────────────────────

    @Test
    void markAsRead_sentNotification_transitionsToRead() {
        Notification notification = buildNotification("NOTIF-001", "SENT");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Notification result = useCase.markAsRead("NOTIF-001");

        assertEquals("READ", result.getStatus());
    }

    @Test
    void markAsRead_pendingNotification_throwsIllegalState() {
        Notification notification = buildNotification("NOTIF-001", "PENDING");
        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(notification));

        assertThrows(IllegalStateException.class, () -> useCase.markAsRead("NOTIF-001"));
    }

    // ─── updateNotification ──────────────────────────────────────────────────

    @Test
    void updateNotification_pending_updatesSuccessfully() {
        Notification existing = buildNotification("NOTIF-001", "PENDING");
        Notification updated = buildNotification("NOTIF-001", null);
        updated.setMessage("Mensaje actualizado");

        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(existing));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Notification result = useCase.updateNotification("NOTIF-001", updated);

        assertEquals("PENDING", result.getStatus());
        assertEquals("Mensaje actualizado", result.getMessage());
    }

    @Test
    void updateNotification_notPending_throwsIllegalState() {
        Notification existing = buildNotification("NOTIF-001", "SENT");
        Notification updated = buildNotification("NOTIF-001", null);

        when(gateway.findByCode("NOTIF-001")).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> useCase.updateNotification("NOTIF-001", updated));
    }

    @Test
    void updateNotification_notFound_throwsNoSuchElement() {
        when(gateway.findByCode("NOTIF-XXX")).thenReturn(Optional.empty());
        Notification updated = buildNotification("NOTIF-XXX", null);

        assertThrows(NoSuchElementException.class, () -> useCase.updateNotification("NOTIF-XXX", updated));
    }

    // ─── listByRecipient / listByStatus / listAll ────────────────────────────

    @Test
    void listNotifications_returnsAll() {
        when(gateway.findAll()).thenReturn(List.of(
                buildNotification("N1", "SENT"),
                buildNotification("N2", "PENDING")));

        List<Notification> result = useCase.listNotifications();
        assertEquals(2, result.size());
    }

    @Test
    void listByRecipient_returnsFilteredNotifications() {
        when(gateway.findByRecipientDocument("CC123456")).thenReturn(List.of(
                buildNotification("N1", "SENT"),
                buildNotification("N2", "PENDING")));

        List<Notification> result = useCase.listByRecipient("CC123456");
        assertEquals(2, result.size());
    }

    @Test
    void listByStatus_returnsFilteredByStatus() {
        when(gateway.findByStatus("PENDING")).thenReturn(List.of(
                buildNotification("N1", "PENDING")));

        List<Notification> result = useCase.listByStatus("PENDING");
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    // ─── deleteNotification ──────────────────────────────────────────────────

    @Test
    void deleteNotification_existingCode_callsGateway() {
        when(gateway.existsByCode("NOTIF-001")).thenReturn(true);
        useCase.deleteNotificationByCode("NOTIF-001");
        verify(gateway).deleteByCode("NOTIF-001");
    }

    @Test
    void deleteNotification_missingCode_throwsNoSuchElement() {
        when(gateway.existsByCode("NOTIF-XXX")).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> useCase.deleteNotificationByCode("NOTIF-XXX"));
    }
}
