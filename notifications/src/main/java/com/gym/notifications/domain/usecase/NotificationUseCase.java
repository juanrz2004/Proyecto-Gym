package com.gym.notifications.domain.usecase;

import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.domain.model.gateway.NotificationGateway;
import com.gym.notifications.domain.model.gateway.SmsGateway;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class NotificationUseCase {

    private final NotificationGateway gateway;
    private final SmsGateway smsGateway;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NotificationUseCase(NotificationGateway gateway, SmsGateway smsGateway) {
        this.gateway = gateway;
        this.smsGateway = smsGateway;
    }

    /**
     * Crea y guarda una nueva notificación con estado PENDING.
     */
    public Notification saveNotification(Notification notification) {
        String code = notification.getCode();
        if (gateway.existsByCode(code)) {
            throw new IllegalArgumentException("Ya existe un registro con code: " + code);
        }
        notification.setStatus("PENDING");
        notification.setCreatedAt(LocalDateTime.now().format(FORMATTER));
        notification.setSentAt(null);
        return gateway.save(notification);
    }

    /**
     * Obtiene una notificación por su código.
     */
    public Notification getNotificationByCode(String code) {
        return gateway.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con code: " + code));
    }

    /**
     * Lista todas las notificaciones.
     */
    public List<Notification> listNotifications() {
        return gateway.findAll();
    }

    /**
     * Lista notificaciones por documento del destinatario.
     */
    public List<Notification> listByRecipient(String recipientDocument) {
        return gateway.findByRecipientDocument(recipientDocument);
    }

    /**
     * Filtra notificaciones por estado (PENDING, SENT, FAILED, READ).
     */
    public List<Notification> listByStatus(String status) {
        return gateway.findByStatus(status);
    }

    /**
     * Actualiza el contenido de una notificación (solo si está PENDING).
     */
    public Notification updateNotification(String code, Notification updated) {
        Notification existing = gateway.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con code: " + code));

        if (!"PENDING".equals(existing.getStatus())) {
            throw new IllegalStateException(
                    "Solo se pueden editar notificaciones en estado PENDING. Estado actual: " + existing.getStatus());
        }

        updated.setCode(code);
        updated.setStatus(existing.getStatus());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setSentAt(existing.getSentAt());
        return gateway.save(updated);
    }

    /**
     * Envía la notificación como SMS via Twilio.
     * Transición de estado: PENDING → SENT (o FAILED si Twilio lanza excepción).
     *
     * El mensaje SMS incluye el asunto y el cuerpo del mensaje.
     * El número de teléfono del destinatario debe estar en formato E.164 (+573001234567).
     */
    public Notification sendNotification(String code) {
        Notification notification = gateway.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con code: " + code));

        if ("SENT".equals(notification.getStatus())) {
            throw new IllegalStateException("La notificación ya fue enviada en: " + notification.getSentAt());
        }
        if ("FAILED".equals(notification.getStatus())) {
            throw new IllegalStateException("La notificación está en estado FAILED y no puede reenviarse.");
        }

        try {
            String smsBody = "[" + notification.getSubject() + "] " + notification.getMessage();
            smsGateway.sendSms(notification.getRecipientPhone(), smsBody);
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now().format(FORMATTER));
        } catch (Exception e) {
            notification.setStatus("FAILED");
            gateway.save(notification);
            throw new IllegalStateException("Error al enviar el SMS vía Twilio: " + e.getMessage());
        }

        return gateway.save(notification);
    }

    /**
     * Marca una notificación SENT como leída (READ).
     */
    public Notification markAsRead(String code) {
        Notification notification = gateway.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con code: " + code));

        if (!"SENT".equals(notification.getStatus())) {
            throw new IllegalStateException(
                    "Solo se pueden marcar como leídas notificaciones en estado SENT. Estado actual: " + notification.getStatus());
        }

        notification.setStatus("READ");
        return gateway.save(notification);
    }

    /**
     * Elimina una notificación por su código.
     */
    public void deleteNotificationByCode(String code) {
        if (!gateway.existsByCode(code)) {
            throw new NoSuchElementException("No existe registro con code: " + code);
        }
        gateway.deleteByCode(code);
    }
}
