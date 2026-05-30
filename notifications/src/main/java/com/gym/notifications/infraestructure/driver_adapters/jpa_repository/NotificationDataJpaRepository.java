package com.gym.notifications.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationDataJpaRepository extends JpaRepository<NotificationData, String> {
    Optional<NotificationData> findByCode(String code);
    List<NotificationData> findByRecipientDocument(String recipientDocument);
    List<NotificationData> findByStatus(String status);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
