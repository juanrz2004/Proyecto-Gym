package com.gym.attendance.domain.model.gateway;

import com.gym.attendance.domain.model.Attendance;
import java.util.List;
import java.util.Optional;

public interface AttendanceGateway {
    Attendance save(Attendance attendance);
    Optional<Attendance> findByCode(String code);
    List<Attendance> findAll();
    void deleteByCode(String code);
    boolean existsByCode(String code);
}
