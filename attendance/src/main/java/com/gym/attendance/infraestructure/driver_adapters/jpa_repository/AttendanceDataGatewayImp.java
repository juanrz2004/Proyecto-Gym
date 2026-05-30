package com.gym.attendance.infraestructure.driver_adapters.jpa_repository;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.model.gateway.AttendanceGateway;
import com.gym.attendance.infraestructure.mapper.AttendanceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceDataGatewayImp implements AttendanceGateway {
    private final AttendanceDataJpaRepository repository;
    private final AttendanceMapper mapper;

    public AttendanceDataGatewayImp(AttendanceDataJpaRepository repository, AttendanceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Attendance save(Attendance attendance) {
        return mapper.toDomain(repository.save(mapper.toData(attendance)));
    }

    @Override
    public Optional<Attendance> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public List<Attendance> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByCode(String code) {
        repository.deleteByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
