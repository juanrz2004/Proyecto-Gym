package com.gym.attendance.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class AttendanceData {
    @Id

private String code;

private String memberDocument;

private String classCode;

private String attendanceDate;

private String status;

    public AttendanceData() {}

    public AttendanceData(String code, String memberDocument, String classCode, String attendanceDate, String status) {
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
