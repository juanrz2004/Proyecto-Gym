package com.gym.notifications.application.config;

import com.gym.notifications.domain.model.gateway.NotificationGateway;
import com.gym.notifications.domain.model.gateway.SmsGateway;
import com.gym.notifications.domain.usecase.NotificationUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    @Test
    void notificationUseCase_bean_isCreatedCorrectly() {
        UseCaseConfig config = new UseCaseConfig();
        NotificationGateway gatewayMock = mock(NotificationGateway.class);
        SmsGateway smsMock = mock(SmsGateway.class);

        NotificationUseCase useCase = config.notificationUseCase(gatewayMock, smsMock);

        assertThat(useCase).isNotNull();
    }

    @Test
    void notificationUseCase_withDifferentMocks_returnsNewInstance() {
        UseCaseConfig config = new UseCaseConfig();
        NotificationGateway g1 = mock(NotificationGateway.class);
        NotificationGateway g2 = mock(NotificationGateway.class);
        SmsGateway s1 = mock(SmsGateway.class);
        SmsGateway s2 = mock(SmsGateway.class);

        NotificationUseCase uc1 = config.notificationUseCase(g1, s1);
        NotificationUseCase uc2 = config.notificationUseCase(g2, s2);

        assertThat(uc1).isNotNull();
        assertThat(uc2).isNotNull();
        assertThat(uc1).isNotSameAs(uc2);
    }
}