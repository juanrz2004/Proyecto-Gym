package com.gym.attendance.domain.usecase;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.infraestructure.driver_adapters.jpa_repository.AttendanceData;
import com.gym.attendance.application.dto.AttendanceRequestDTO;
import com.gym.attendance.application.dto.AttendanceResponseDTO;
import com.gym.attendance.application.dto.AttendanceUpdateDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceModelTest {

    @Test
    void attendance_defaultConstructorAndSetters() {
        Attendance a = new Attendance();
        a.setCode("ATT-1");
        a.setMemberDocument("CC123");
        a.setClassCode("CLS-1");
        a.setAttendanceDate("2024-01-01");
        a.setStatus("PRESENT");

        assertEquals("ATT-1", a.getCode());
        assertEquals("CC123", a.getMemberDocument());
        assertEquals("CLS-1", a.getClassCode());
        assertEquals("2024-01-01", a.getAttendanceDate());
        assertEquals("PRESENT", a.getStatus());
    }

    @Test
    void attendanceData_defaultConstructorAndSetters() {
        AttendanceData d = new AttendanceData();
        d.setCode("ATT-1");
        d.setMemberDocument("CC123");
        d.setClassCode("CLS-1");
        d.setAttendanceDate("2024-01-01");
        d.setStatus("PRESENT");

        assertEquals("ATT-1", d.getCode());
        assertEquals("CC123", d.getMemberDocument());
        assertEquals("CLS-1", d.getClassCode());
        assertEquals("2024-01-01", d.getAttendanceDate());
        assertEquals("PRESENT", d.getStatus());
    }

    @Test
    void attendanceRequestDTO_defaultConstructorAndSetters() {
        AttendanceRequestDTO dto = new AttendanceRequestDTO();
        dto.setCode("ATT-1");
        dto.setMemberDocument("CC123");
        dto.setClassCode("CLS-1");
        dto.setAttendanceDate("2024-01-01");
        dto.setStatus("PRESENT");

        assertEquals("ATT-1", dto.getCode());
        assertEquals("CC123", dto.getMemberDocument());
        assertEquals("CLS-1", dto.getClassCode());
        assertEquals("2024-01-01", dto.getAttendanceDate());
        assertEquals("PRESENT", dto.getStatus());
    }

    @Test
    void attendanceResponseDTO_defaultConstructorAndSetters() {
        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        dto.setCode("ATT-1");
        dto.setMemberDocument("CC123");
        dto.setClassCode("CLS-1");
        dto.setAttendanceDate("2024-01-01");
        dto.setStatus("PRESENT");

        assertEquals("ATT-1", dto.getCode());
        assertEquals("CC123", dto.getMemberDocument());
        assertEquals("CLS-1", dto.getClassCode());
        assertEquals("2024-01-01", dto.getAttendanceDate());
        assertEquals("PRESENT", dto.getStatus());
    }

    @Test
    void attendanceUpdateDTO_defaultConstructorAndSetters() {
        AttendanceUpdateDTO dto = new AttendanceUpdateDTO();
        dto.setCode("ATT-1");
        dto.setMemberDocument("CC123");
        dto.setClassCode("CLS-1");
        dto.setAttendanceDate("2024-01-01");
        dto.setStatus("ABSENT");

        assertEquals("ATT-1", dto.getCode());
        assertEquals("CC123", dto.getMemberDocument());
        assertEquals("CLS-1", dto.getClassCode());
        assertEquals("2024-01-01", dto.getAttendanceDate());
        assertEquals("ABSENT", dto.getStatus());
    }
}