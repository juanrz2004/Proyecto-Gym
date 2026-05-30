package com.gym.attendance.application.config;

import com.gym.attendance.domain.model.gateway.AttendanceGateway;
import com.gym.attendance.domain.usecase.AttendanceUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    @Test
    void attendanceUseCase_beanCreated_notNull() {
        UseCaseConfig config = new UseCaseConfig();
        AttendanceGateway gateway = mock(AttendanceGateway.class);

        AttendanceUseCase useCase = config.attendanceUseCase(gateway);

        assertNotNull(useCase);
    }
}