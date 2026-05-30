package com.gym.attendance.application.dto;

    import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public class AttendanceRequestDTO {
        @NotBlank(message = "El campo codigo es obligatorio")

    private String code;

    @NotBlank(message = "El campo documento socio es obligatorio")

    private String memberDocument;

    @NotBlank(message = "El campo codigo clase es obligatorio")

    private String classCode;

    @NotBlank(message = "El campo fecha asistencia es obligatorio")

    private String attendanceDate;

    @NotBlank(message = "El campo estado es obligatorio")

    private String status;

        public AttendanceRequestDTO() {}

        public AttendanceRequestDTO(String code, String memberDocument, String classCode, String attendanceDate, String status) {
            this.code = code;
        this.memberDocument = memberDocument;
        this.classCode = classCode;
        this.attendanceDate = attendanceDate;
        this.status = status;
        }

        public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getMemberDocument() { return memberDocument; }

    public void setMemberDocument(String memberDocument) { this.memberDocument = memberDocument; }

    public String getClassCode() { return classCode; }

    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getAttendanceDate() { return attendanceDate; }

    public void setAttendanceDate(String attendanceDate) { this.attendanceDate = attendanceDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
    }
