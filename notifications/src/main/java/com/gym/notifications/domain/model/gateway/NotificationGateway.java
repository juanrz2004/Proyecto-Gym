package com.gym.notifications.domain.model.gateway;

import com.gym.notifications.domain.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationGateway {
    Notification save(Notification notification);
    Optional<Notification> findByCode(String code);
    List<Notification> findAll();
    List<Notification> findByRecipientDocument(String recipientDocument);
    List<Notification> findByStatus(String status);
    void deleteByCode(String code);
    boolean existsByCode(String code);
}
