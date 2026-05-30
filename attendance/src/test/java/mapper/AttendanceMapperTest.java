package com.gym.attendance.infraestructure.mapper;

import com.gym.attendance.application.dto.AttendanceRequestDTO;
import com.gym.attendance.application.dto.AttendanceResponseDTO;
import com.gym.attendance.application.dto.AttendanceUpdateDTO;
import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.infraestructure.driver_adapters.jpa_repository.AttendanceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceMapperTest {

    private AttendanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AttendanceMapper();
    }

    @Test
    void toAttendanceFromDTO_mapsAllFields() {
        AttendanceRequestDTO dto = new AttendanceRequestDTO("ATT-1", "CC123", "CLS-1", "2024-01-01", "PRESENT");

        Attendance result = mapper.toAttendanceFromDTO(dto);

        assertEquals("ATT-1", result.getCode());
        assertEquals("CC123", result.getMemberDocument());
        assertEquals("CLS-1", result.getClassCode());
        assertEquals("2024-01-01", result.getAttendanceDate());
        assertEquals("PRESENT", result.getStatus());
    }

    @Test
    void toAttendanceFromUpdateDTO_mapsAllFields() {
        AttendanceUpdateDTO dto = new AttendanceUpdateDTO("ATT-1", "CC123", "CLS-1", "2024-01-01", "ABSENT");

        Attendance result = mapper.toAttendanceFromUpdateDTO(dto);

        assertEquals("ATT-1", result.getCode());
        assertEquals("ABSENT", result.getStatus());
    }

    @Test
    void toAttendanceResponseDTO_mapsAllFields() {
        Attendance a = new Attendance("ATT-1", "CC123", "CLS-1", "2024-01-01", "PRESENT");

        AttendanceResponseDTO result = mapper.toAttendanceResponseDTO(a);

        assertEquals("ATT-1", result.getCode());
        assertEquals("CC123", result.getMemberDocument());
        assertEquals("CLS-1", result.getClassCode());
        assertEquals("2024-01-01", result.getAttendanceDate());
        assertEquals("PRESENT", result.getStatus());
    }

    @Test
    void toData_mapsAllFields() {
        Attendance a = new Attendance("ATT-1", "CC123", "CLS-1", "2024-01-01", "PRESENT");

        AttendanceData result = mapper.toData(a);

        assertEquals("ATT-1", result.getCode());
        assertEquals("CC123", result.getMemberDocument());
        assertEquals("CLS-1", result.getClassCode());
        assertEquals("2024-01-01", result.getAttendanceDate());
        assertEquals("PRESENT", result.getStatus());
    }

    @Test
    void toDomain_mapsAllFields() {
        AttendanceData data = new AttendanceData("ATT-1", "CC123", "CLS-1", "2024-01-01", "PRESENT");

        Attendance result = mapper.toDomain(data);

        assertEquals("ATT-1", result.getCode());
        assertEquals("CC123", result.getMemberDocument());
        assertEquals("CLS-1", result.getClassCode());
        assertEquals("2024-01-01", result.getAttendanceDate());
        assertEquals("PRESENT", result.getStatus());
    }
}