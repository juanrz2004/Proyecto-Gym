package com.gym.members.application.config;

import com.gym.members.domain.model.gateway.MemberGateway;
import com.gym.members.domain.usecase.MemberUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public MemberUseCase memberUseCase(MemberGateway gateway) {
        return new MemberUseCase(gateway);
    }
}
