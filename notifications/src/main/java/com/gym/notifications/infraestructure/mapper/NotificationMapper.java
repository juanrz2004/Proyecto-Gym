package com.gym.notifications.infraestructure.mapper;

import com.gym.notifications.application.dto.NotificationRequestDTO;
import com.gym.notifications.application.dto.NotificationResponseDTO;
import com.gym.notifications.application.dto.NotificationUpdateDTO;
import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.infraestructure.driver_adapters.jpa_repository.NotificationData;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toNotificationFromDTO(NotificationRequestDTO dto) {
        return new Notification(dto.getCode(), dto.getRecipientDocument(), dto.getRecipientPhone(),
                dto.getType(), dto.getSubject(), dto.getMessage(), null, null, null);
    }

    public Notification toNotificationFromUpdateDTO(NotificationUpdateDTO dto) {
        return new Notification(dto.getCode(), dto.getRecipientDocument(), dto.getRecipientPhone(),
                dto.getType(), dto.getSubject(), dto.getMessage(), null, null, null);
    }

    public NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        return new NotificationResponseDTO(notification.getCode(), notification.getRecipientDocument(),
                notification.getRecipientPhone(), notification.getType(), notification.getSubject(),
                notification.getMessage(), notification.getStatus(),
                notification.getCreatedAt(), notification.getSentAt());
    }

    public NotificationData toData(Notification notification) {
        return new NotificationData(notification.getCode(), notification.getRecipientDocument(),
                notification.getRecipientPhone(), notification.getType(), notification.getSubject(),
                notification.getMessage(), notification.getStatus(),
                notification.getCreatedAt(), notification.getSentAt());
    }

    public Notification toDomain(NotificationData data) {
        return new Notification(data.getCode(), data.getRecipientDocument(),
                data.getRecipientPhone(), data.getType(), data.getSubject(),
                data.getMessage(), data.getStatus(),
                data.getCreatedAt(), data.getSentAt());
    }
}
