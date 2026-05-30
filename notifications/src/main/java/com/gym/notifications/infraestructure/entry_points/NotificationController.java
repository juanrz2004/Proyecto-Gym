package com.gym.notifications.infraestructure.entry_points;

import com.gym.notifications.application.dto.NotificationRequestDTO;
import com.gym.notifications.application.dto.NotificationResponseDTO;
import com.gym.notifications.application.dto.NotificationUpdateDTO;
import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.domain.usecase.NotificationUseCase;
import com.gym.notifications.infraestructure.mapper.NotificationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for the Notifications microservice.
 *
 * Base path: /api/gym/notification
 *
 * CRUD endpoints:
 *   POST   /save                            → Create a notification (PENDING)
 *   GET    /get/{code}                      → Get by code
 *   GET    /list                            → List all notifications
 *   PUT    /update/{code}                   → Update a PENDING notification
 *   DELETE /delete/{code}                   → Delete a notification
 *
 * Special endpoints:
 *   PATCH  /send/{code}                     → Send a notification (PENDING → SENT)
 *   PATCH  /read/{code}                     → Mark as read (SENT → READ)
 *   GET    /recipient/{recipientDocument}   → List notifications for a member
 *   GET    /status/{status}                 → Filter by status
 */
@RestController
@RequestMapping("/api/gym/notification")
public class NotificationController {

    private final NotificationUseCase useCase;
    private final NotificationMapper mapper;

    public NotificationController(NotificationUseCase useCase, NotificationMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    // ── CRUD ──────────────────────────────────────────────────────────────

    /**
     * Creates a new notification with status PENDING.
     * The createdAt timestamp is set automatically by the use case.
     */
    @PostMapping("/save")
    public ResponseEntity<NotificationResponseDTO> saveNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) {
        Notification notification = useCase.saveNotification(mapper.toNotificationFromDTO(requestDTO));
        return new ResponseEntity<>(mapper.toNotificationResponseDTO(notification), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single notification by its unique code.
     */
    @GetMapping("/get/{code}")
    public ResponseEntity<NotificationResponseDTO> getNotification(@PathVariable String code) {
        Notification notification = useCase.getNotificationByCode(code);
        return ResponseEntity.ok(mapper.toNotificationResponseDTO(notification));
    }

    /**
     * Returns all notifications in the system.
     */
    @GetMapping("/list")
    public ResponseEntity<List<NotificationResponseDTO>> listNotifications() {
        return ResponseEntity.ok(
                useCase.listNotifications().stream()
                        .map(mapper::toNotificationResponseDTO)
                        .toList());
    }

    /**
     * Updates the content of a notification.
     * Only notifications in PENDING status can be edited.
     */
    @PutMapping("/update/{code}")
    public ResponseEntity<NotificationResponseDTO> updateNotification(
            @PathVariable String code,
            @Valid @RequestBody NotificationUpdateDTO updateDTO) {
        Notification notification = useCase.updateNotification(code, mapper.toNotificationFromUpdateDTO(updateDTO));
        return ResponseEntity.ok(mapper.toNotificationResponseDTO(notification));
    }

    /**
     * Deletes a notification by its code.
     */
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable String code) {
        useCase.deleteNotificationByCode(code);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Notificación " + code + " eliminada correctamente");
        return ResponseEntity.ok(response);
    }

    // ── Special endpoints ─────────────────────────────────────────────────

    /**
     * Sends a notification: transitions status from PENDING → SENT
     * and records the sentAt timestamp.
     * Simulates delivery (e.g. email/SMS dispatch).
     */
    @PatchMapping("/send/{code}")
    public ResponseEntity<NotificationResponseDTO> sendNotification(@PathVariable String code) {
        Notification notification = useCase.sendNotification(code);
        return ResponseEntity.ok(mapper.toNotificationResponseDTO(notification));
    }

    /**
     * Marks a sent notification as READ by the member.
     * Transitions status from SENT → READ.
     */
    @PatchMapping("/read/{code}")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable String code) {
        Notification notification = useCase.markAsRead(code);
        return ResponseEntity.ok(mapper.toNotificationResponseDTO(notification));
    }

    /**
     * Returns all notifications for a specific member,
     * identified by their document number.
     */
    @GetMapping("/recipient/{recipientDocument}")
    public ResponseEntity<List<NotificationResponseDTO>> listByRecipient(
            @PathVariable String recipientDocument) {
        return ResponseEntity.ok(
                useCase.listByRecipient(recipientDocument).stream()
                        .map(mapper::toNotificationResponseDTO)
                        .toList());
    }

    /**
     * Filters notifications by status.
     * Valid values: PENDING, SENT, FAILED, READ
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponseDTO>> listByStatus(@PathVariable String status) {
        return ResponseEntity.ok(
                useCase.listByStatus(status).stream()
                        .map(mapper::toNotificationResponseDTO)
                        .toList());
    }
}
