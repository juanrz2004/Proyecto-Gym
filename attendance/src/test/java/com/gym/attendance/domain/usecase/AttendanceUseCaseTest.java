package com.gym.attendance.domain.usecase;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.model.gateway.AttendanceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceUseCaseTest {
    private AttendanceGateway gateway;
    private AttendanceUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(AttendanceGateway.class);
        useCase = new AttendanceUseCase(gateway);
    }

    @Test
    void shouldSaveWhenIdentifierDoesNotExist() {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(gateway.existsByCode(attendance.getCode())).thenReturn(false);
        when(gateway.save(attendance)).thenReturn(attendance);

        Attendance saved = useCase.saveAttendance(attendance);

        assertEquals(attendance.getCode(), saved.getCode());
        verify(gateway).save(attendance);
    }

    @Test
    void shouldThrowWhenIdentifierAlreadyExists() {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(gateway.existsByCode(attendance.getCode())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.saveAttendance(attendance));
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldFindByIdentifier() {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(gateway.findByCode(attendance.getCode())).thenReturn(Optional.of(attendance));

        Attendance found = useCase.getAttendanceByCode(attendance.getCode());

        assertEquals(attendance.getCode(), found.getCode());
    }

    @Test
    void shouldThrowWhenIdentifierDoesNotExist() {
        when(gateway.findByCode("missing")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> useCase.getAttendanceByCode("missing"));
    }

    @Test
    void shouldListAll() {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(gateway.findAll()).thenReturn(List.of(attendance));

        assertEquals(1, useCase.listAttendances().size());
    }
}
