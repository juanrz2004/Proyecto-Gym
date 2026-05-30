package com.gym.attendance.infraestructure.entry_points;

import com.gym.attendance.application.dto.AttendanceRequestDTO;
import com.gym.attendance.application.dto.AttendanceResponseDTO;
import com.gym.attendance.application.dto.AttendanceUpdateDTO;
import com.gym.attendance.domain.model.Attendance;
import com.gym.attendance.domain.usecase.AttendanceUseCase;
import com.gym.attendance.infraestructure.mapper.AttendanceMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gym/attendance")
public class AttendanceController {
    private final AttendanceUseCase useCase;
    private final AttendanceMapper mapper;

    public AttendanceController(AttendanceUseCase useCase, AttendanceMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public ResponseEntity<AttendanceResponseDTO> saveAttendance(@Valid @RequestBody AttendanceRequestDTO requestDTO) {
        Attendance attendance = useCase.saveAttendance(mapper.toAttendanceFromDTO(requestDTO));
        return new ResponseEntity<>(mapper.toAttendanceResponseDTO(attendance), HttpStatus.CREATED);
    }

    @GetMapping("/get/{code}")
    public ResponseEntity<AttendanceResponseDTO> getAttendance(@PathVariable String code) {
        Attendance attendance = useCase.getAttendanceByCode(code);
        return ResponseEntity.ok(mapper.toAttendanceResponseDTO(attendance));
    }

    @GetMapping("/list")
    public ResponseEntity<List<AttendanceResponseDTO>> listAttendances() {
        return ResponseEntity.ok(useCase.listAttendances().stream().map(mapper::toAttendanceResponseDTO).toList());
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(@PathVariable String code, @Valid @RequestBody AttendanceUpdateDTO updateDTO) {
        Attendance attendance = useCase.updateAttendance(code, mapper.toAttendanceFromUpdateDTO(updateDTO));
        return ResponseEntity.ok(mapper.toAttendanceResponseDTO(attendance));
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<Map<String, String>> deleteAttendance(@PathVariable String code) {
        useCase.deleteAttendanceByCode(code);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Registro " + code + " eliminado correctamente");
        return ResponseEntity.ok(response);
    }
}
