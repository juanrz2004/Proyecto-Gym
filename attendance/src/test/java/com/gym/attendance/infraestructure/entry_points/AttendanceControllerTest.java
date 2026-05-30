package com.gym.attendance.infraestructure.entry_points;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.usecase.AttendanceUseCase;
import com.gym.attendance.infraestructure.mapper.AttendanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AttendanceController(useCase, mapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Attendance build(String code) {
        return new Attendance(code, "CC123456", "CLASS-01", "2024-01-10", "PRESENT");
    }

    private String json(String code, String memberDoc, String classCode, String date, String status) {
        return "{\"code\":\"" + code + "\","
                + "\"memberDocument\":\"" + memberDoc + "\","
                + "\"classCode\":\"" + classCode + "\","
                + "\"attendanceDate\":\"" + date + "\","
                + "\"status\":\"" + status + "\"}";
    }

    @Test
    void saveAttendance_valid_returns201() throws Exception {
        when(useCase.saveAttendance(any())).thenReturn(build("ATT-001"));

        mockMvc.perform(post("/api/gym/attendance/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("ATT-001", "CC123456", "CLASS-01", "2024-01-10", "PRESENT")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ATT-001"))
                .andExpect(jsonPath("$.status").value("PRESENT"));
    }

    @Test
    void saveAttendance_duplicateCode_returns400() throws Exception {
        when(useCase.saveAttendance(any()))
                .thenThrow(new IllegalArgumentException("Ya existe un registro con code: ATT-001"));

        mockMvc.perform(post("/api/gym/attendance/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("ATT-001", "CC123456", "CLASS-01", "2024-01-10", "PRESENT")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ya existe un registro con code: ATT-001"));
    }

    @Test
    void saveAttendance_missingCode_returns400() throws Exception {
        mockMvc.perform(post("/api/gym/attendance/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberDocument\":\"CC123456\",\"classCode\":\"CLASS-01\",\"attendanceDate\":\"2024-01-10\",\"status\":\"PRESENT\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveAttendance_missingMemberDocument_returns400() throws Exception {
        mockMvc.perform(post("/api/gym/attendance/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"ATT-001\",\"classCode\":\"CLASS-01\",\"attendanceDate\":\"2024-01-10\",\"status\":\"PRESENT\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAttendance_existing_returns200() throws Exception {
        when(useCase.getAttendanceByCode("ATT-001")).thenReturn(build("ATT-001"));

        mockMvc.perform(get("/api/gym/attendance/get/ATT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ATT-001"))
                .andExpect(jsonPath("$.memberDocument").value("CC123456"));
    }

    @Test
    void getAttendance_missing_returns404() throws Exception {
        when(useCase.getAttendanceByCode("MISSING"))
                .thenThrow(new NoSuchElementException("No existe registro con code: MISSING"));

        mockMvc.perform(get("/api/gym/attendance/get/MISSING"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe registro con code: MISSING"));
    }

    @Test
    void listAttendances_returns200WithList() throws Exception {
        when(useCase.listAttendances()).thenReturn(List.of(build("A1"), build("A2")));

        mockMvc.perform(get("/api/gym/attendance/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listAttendances_empty_returns200EmptyArray() throws Exception {
        when(useCase.listAttendances()).thenReturn(List.of());

        mockMvc.perform(get("/api/gym/attendance/list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void updateAttendance_valid_returns200() throws Exception {
        Attendance updated = build("ATT-001");
        updated.setStatus("ABSENT");
        when(useCase.updateAttendance(eq("ATT-001"), any())).thenReturn(updated);

        mockMvc.perform(put("/api/gym/attendance/update/ATT-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("ATT-001", "CC123456", "CLASS-01", "2024-01-10", "ABSENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ABSENT"));
    }

    @Test
    void updateAttendance_missing_returns404() throws Exception {
        when(useCase.updateAttendance(eq("MISSING"), any()))
                .thenThrow(new NoSuchElementException("No existe registro con code: MISSING"));

        mockMvc.perform(put("/api/gym/attendance/update/MISSING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("MISSING", "CC123456", "CLASS-01", "2024-01-10", "PRESENT")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAttendance_invalidBody_returns400() throws Exception {
        mockMvc.perform(put("/api/gym/attendance/update/ATT-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberDocument\":\"CC123456\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAttendance_existing_returns200WithMessage() throws Exception {
        doNothing().when(useCase).deleteAttendanceByCode("ATT-001");

        mockMvc.perform(delete("/api/gym/attendance/delete/ATT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro ATT-001 eliminado correctamente"));
    }

    @Test
    void deleteAttendance_missing_returns404() throws Exception {
        doThrow(new NoSuchElementException("No existe registro con code: MISSING"))
                .when(useCase).deleteAttendanceByCode("MISSING");

        mockMvc.perform(delete("/api/gym/attendance/delete/MISSING"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe registro con code: MISSING"));
    }
}