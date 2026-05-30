package com.gym.attendance.infraestructure.mapper;

import com.gym.attendance.application.dto.AttendanceRequestDTO;
import com.gym.attendance.application.dto.AttendanceResponseDTO;
import com.gym.attendance.application.dto.AttendanceUpdateDTO;
import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.infraestructure.driver_adapters.jpa_repository.AttendanceData;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {
    public Attendance toAttendanceFromDTO(AttendanceRequestDTO dto) {
        return new Attendance(dto.getCode(), dto.getMemberDocument(), dto.getClassCode(), dto.getAttendanceDate(), dto.getStatus());
    }

    public Attendance toAttendanceFromUpdateDTO(AttendanceUpdateDTO dto) {
        return new Attendance(dto.getCode(), dto.getMemberDocument(), dto.getClassCode(), dto.getAttendanceDate(), dto.getStatus());
    }

    public AttendanceResponseDTO toAttendanceResponseDTO(Attendance attendance) {
        return new AttendanceResponseDTO(attendance.getCode(), attendance.getMemberDocument(), attendance.getClassCode(), attendance.getAttendanceDate(), attendance.getStatus());
    }

    public AttendanceData toData(Attendance attendance) {
        return new AttendanceData(attendance.getCode(), attendance.getMemberDocument(), attendance.getClassCode(), attendance.getAttendanceDate(), attendance.getStatus());
    }

    public Attendance toDomain(AttendanceData data) {
        return new Attendance(data.getCode(), data.getMemberDocument(), data.getClassCode(), data.getAttendanceDate(), data.getStatus());
    }
}
