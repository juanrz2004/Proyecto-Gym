package com.gym.attendance.domain.usecase;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.model.gateway.AttendanceGateway;

import java.util.List;
import java.util.NoSuchElementException;

public class AttendanceUseCase {
    private final AttendanceGateway gateway;

    public AttendanceUseCase(AttendanceGateway gateway) {
        this.gateway = gateway;
    }

    public Attendance saveAttendance(Attendance attendance) {
        String code = attendance.getCode();
        if (gateway.existsByCode(code)) {
            throw new IllegalArgumentException("Ya existe un registro con code: " + code);
        }
        return gateway.save(attendance);
    }

    public Attendance getAttendanceByCode(String code) {
        return gateway.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con code: " + code));
    }

    public List<Attendance> listAttendances() {
        return gateway.findAll();
    }

    public Attendance updateAttendance(String code, Attendance attendance) {
        if (!gateway.existsByCode(code)) {
            throw new NoSuchElementException("No existe registro con code: " + code);
        }
        attendance.setCode(code);
        return gateway.save(attendance);
    }

    public void deleteAttendanceByCode(String code) {
        if (!gateway.existsByCode(code)) {
            throw new NoSuchElementException("No existe registro con code: " + code);
        }
        gateway.deleteByCode(code);
    }
}
