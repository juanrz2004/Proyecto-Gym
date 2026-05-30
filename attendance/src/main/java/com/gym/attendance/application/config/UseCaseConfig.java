package com.gym.attendance.application.config;

import com.gym.attendance.domain.model.gateway.AttendanceGateway;
import com.gym.attendance.domain.usecase.AttendanceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public AttendanceUseCase attendanceUseCase(AttendanceGateway gateway) {
        return new AttendanceUseCase(gateway);
    }
}
