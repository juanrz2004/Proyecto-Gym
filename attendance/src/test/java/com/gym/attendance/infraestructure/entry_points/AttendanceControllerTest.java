package com.gym.attendance.infraestructure.entry_points;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.usecase.AttendanceUseCase;
import com.gym.attendance.infraestructure.mapper.AttendanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AttendanceControllerTest {
    private AttendanceUseCase useCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        useCase = mock(AttendanceUseCase.class);
        AttendanceMapper mapper = new AttendanceMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new AttendanceController(useCase, mapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateAttendance() throws Exception {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(useCase.saveAttendance(any(Attendance.class))).thenReturn(attendance);

        mockMvc.perform(post("/api/gym/attendance/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":"code-test",\"memberDocument\":"memberDocument-test",\"classCode\":"classCode-test",\"attendanceDate\":"attendanceDate-test",\"status\":"status-test"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(attendance.getCode()));
    }

    @Test
    void shouldGetAttendance() throws Exception {
        Attendance attendance = new Attendance("code-test", "memberDocument-test", "classCode-test", "attendanceDate-test", "status-test");
        when(useCase.getAttendanceByCode(attendance.getCode())).thenReturn(attendance);

        mockMvc.perform(get("/api/gym/attendance/get/{code}", attendance.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(attendance.getCode()));
    }

    @Test
    void shouldReturnNotFoundWhenMissing() throws Exception {
        when(useCase.getAttendanceByCode("missing")).thenThrow(new NoSuchElementException("No existe"));

        mockMvc.perform(get("/api/gym/attendance/get/{code}", "missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe"));
    }
}
