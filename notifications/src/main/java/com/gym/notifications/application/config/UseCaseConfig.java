package com.gym.notifications.application.config;

import com.gym.notifications.domain.model.gateway.NotificationGateway;
import com.gym.notifications.domain.model.gateway.SmsGateway;
import com.gym.notifications.domain.usecase.NotificationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public NotificationUseCase notificationUseCase(NotificationGateway notificationGateway,
                                                    SmsGateway smsGateway) {
        return new NotificationUseCase(notificationGateway, smsGateway);
    }
}
