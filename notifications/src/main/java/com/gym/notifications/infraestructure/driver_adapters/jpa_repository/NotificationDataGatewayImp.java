package com.gym.notifications.infraestructure.driver_adapters.jpa_repository;

import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.domain.model.gateway.NotificationGateway;
import com.gym.notifications.infraestructure.mapper.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationDataGatewayImp implements NotificationGateway {

    private final NotificationDataJpaRepository repository;
    private final NotificationMapper mapper;

    public NotificationDataGatewayImp(NotificationDataJpaRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        return mapper.toDomain(repository.save(mapper.toData(notification)));
    }

    @Override
    public Optional<Notification> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public List<Notification> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Notification> findByRecipientDocument(String recipientDocument) {
        return repository.findByRecipientDocument(recipientDocument).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Notification> findByStatus(String status) {
        return repository.findByStatus(status).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByCode(String code) {
        repository.deleteByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
