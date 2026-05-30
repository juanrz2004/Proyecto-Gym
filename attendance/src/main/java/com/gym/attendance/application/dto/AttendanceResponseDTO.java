package com.gym.attendance.application.dto;


public class AttendanceResponseDTO {
    private String code;

private String memberDocument;

private String classCode;

private String attendanceDate;

private String status;

    public AttendanceResponseDTO() {}

    public AttendanceResponseDTO(String code, String memberDocument, String classCode, String attendanceDate, String status) {
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
