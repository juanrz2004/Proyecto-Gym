package com.gym.notifications.infraestructure.driver_adapters.jpa_repository;

import com.gym.notifications.domain.model.Notification;
import com.gym.notifications.infraestructure.mapper.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDataGatewayImpTest {

    @Mock
    private NotificationDataJpaRepository repository;

    @Mock
    private NotificationMapper mapper;

    private NotificationDataGatewayImp gateway;

    private Notification sampleDomain;
    private NotificationData sampleData;

    @BeforeEach
    void setUp() {
        gateway = new NotificationDataGatewayImp(repository, mapper);

        sampleDomain = new Notification(
                "NOTIF-001", "CC12345678", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                "PENDING", "2024-01-01 10:00:00", null
        );
        sampleData = new NotificationData(
                "NOTIF-001", "CC12345678", "+573001234567",
                "WELCOME", "Bienvenido", "Tu membresía está activa.",
                "PENDING", "2024-01-01 10:00:00", null
        );
    }

    @Test
    void save_callsRepositoryAndReturnsNotification() {
        when(mapper.toData(sampleDomain)).thenReturn(sampleData);
        when(repository.save(sampleData)).thenReturn(sampleData);
        when(mapper.toDomain(sampleData)).thenReturn(sampleDomain);

        Notification result = gateway.save(sampleDomain);

        assertThat(result.getCode()).isEqualTo("NOTIF-001");
        verify(repository).save(sampleData);
    }

    @Test
    void findByCode_existingCode_returnsOptional() {
        when(repository.findByCode("NOTIF-001")).thenReturn(Optional.of(sampleData));
        when(mapper.toDomain(sampleData)).thenReturn(sampleDomain);

        Optional<Notification> result = gateway.findByCode("NOTIF-001");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("NOTIF-001");
    }

    @Test
    void findByCode_missingCode_returnsEmpty() {
        when(repository.findByCode("NO-EXISTE")).thenReturn(Optional.empty());

        assertThat(gateway.findByCode("NO-EXISTE")).isEmpty();
    }

    @Test
    void findAll_returnsListOfNotifications() {
        when(repository.findAll()).thenReturn(List.of(sampleData));
        when(mapper.toDomain(sampleData)).thenReturn(sampleDomain);

        assertThat(gateway.findAll()).hasSize(1);
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(gateway.findAll()).isEmpty();
    }

    @Test
    void findByRecipientDocument_returnsMatching() {
        when(repository.findByRecipientDocument("CC12345678")).thenReturn(List.of(sampleData));
        when(mapper.toDomain(sampleData)).thenReturn(sampleDomain);

        assertThat(gateway.findByRecipientDocument("CC12345678")).hasSize(1);
    }

    @Test
    void findByStatus_returnsMatching() {
        when(repository.findByStatus("PENDING")).thenReturn(List.of(sampleData));
        when(mapper.toDomain(sampleData)).thenReturn(sampleDomain);

        assertThat(gateway.findByStatus("PENDING")).hasSize(1);
    }

    @Test
    void findByStatus_noMatches_returnsEmpty() {
        when(repository.findByStatus("READ")).thenReturn(List.of());

        assertThat(gateway.findByStatus("READ")).isEmpty();
    }

    @Test
    void deleteByCode_callsRepository() {
        doNothing().when(repository).deleteByCode("NOTIF-001");
        gateway.deleteByCode("NOTIF-001");
        verify(repository).deleteByCode("NOTIF-001");
    }

    @Test
    void existsByCode_returnsTrue() {
        when(repository.existsByCode("NOTIF-001")).thenReturn(true);
        assertThat(gateway.existsByCode("NOTIF-001")).isTrue();
    }

    @Test
    void existsByCode_returnsFalse() {
        when(repository.existsByCode("NO-EXISTE")).thenReturn(false);
        assertThat(gateway.existsByCode("NO-EXISTE")).isFalse();
    }
}