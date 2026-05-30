package com.gym.members.application.config;

import com.gym.members.domain.model.gateway.MemberGateway;
import com.gym.members.domain.usecase.MemberUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    @Test
    void shouldCreateMemberUseCaseBean() {
        MemberGateway gateway = mock(MemberGateway.class);
        UseCaseConfig config = new UseCaseConfig();
        MemberUseCase useCase = config.memberUseCase(gateway);
        assertNotNull(useCase);
    }
}