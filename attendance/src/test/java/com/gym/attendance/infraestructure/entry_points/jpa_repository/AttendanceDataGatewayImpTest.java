package com.gym.attendance.infraestructure.driver_adapters.jpa_repository;

import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.infraestructure.mapper.AttendanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceDataGatewayImpTest {

    private AttendanceDataJpaRepository repository;
    private AttendanceMapper mapper;
    private AttendanceDataGatewayImp gateway;

    @BeforeEach
    void setUp() {
        repository = mock(AttendanceDataJpaRepository.class);
        mapper = new AttendanceMapper();
        gateway = new AttendanceDataGatewayImp(repository, mapper);
    }

    private AttendanceData buildData(String code) {
        return new AttendanceData(code, "CC123456", "CLASS-01", "2024-01-10", "PRESENT");
    }

    private Attendance buildDomain(String code) {
        return new Attendance(code, "CC123456", "CLASS-01", "2024-01-10", "PRESENT");
    }

    @Test
    void save_persistsAndReturnsDomain() {
        when(repository.save(any())).thenReturn(buildData("ATT-001"));

        Attendance result = gateway.save(buildDomain("ATT-001"));

        assertEquals("ATT-001", result.getCode());
        assertEquals("CC123456", result.getMemberDocument());
        verify(repository).save(any());
    }

    @Test
    void findByCode_existing_returnsDomain() {
        when(repository.findByCode("ATT-001")).thenReturn(Optional.of(buildData("ATT-001")));

        Optional<Attendance> result = gateway.findByCode("ATT-001");

        assertTrue(result.isPresent());
        assertEquals("ATT-001", result.get().getCode());
    }

    @Test
    void findByCode_missing_returnsEmpty() {
        when(repository.findByCode("MISSING")).thenReturn(Optional.empty());

        assertFalse(gateway.findByCode("MISSING").isPresent());
    }

    @Test
    void findAll_returnsMappedList() {
        when(repository.findAll()).thenReturn(List.of(buildData("A1"), buildData("A2")));

        List<Attendance> result = gateway.findAll();

        assertEquals(2, result.size());
        assertEquals("A1", result.get(0).getCode());
    }

    @Test
    void deleteByCode_callsRepository() {
        doNothing().when(repository).deleteByCode("ATT-001");

        gateway.deleteByCode("ATT-001");

        verify(repository).deleteByCode("ATT-001");
    }

    @Test
    void existsByCode_true_returnsTrue() {
        when(repository.existsByCode("ATT-001")).thenReturn(true);
        assertTrue(gateway.existsByCode("ATT-001"));
    }

    @Test
    void existsByCode_false_returnsFalse() {
        when(repository.existsByCode("MISSING")).thenReturn(false);
        assertFalse(gateway.existsByCode("MISSING"));
    }
}