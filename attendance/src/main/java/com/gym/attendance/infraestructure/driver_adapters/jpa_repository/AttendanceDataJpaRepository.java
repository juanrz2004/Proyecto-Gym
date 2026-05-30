package com.gym.attendance.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttendanceDataJpaRepository extends JpaRepository<AttendanceData, String> {
    Optional<AttendanceData> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
