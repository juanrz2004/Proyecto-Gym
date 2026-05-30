package com.gym.members.application.dto;


public class MemberResponseDTO {
    private String document;

private String fullName;

private String email;

private String phone;

private String status;

    public MemberResponseDTO() {}

    public MemberResponseDTO(String document, String fullName, String email, String phone, String status) {
        this.document = document;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.status = status;
    }

    public String getDocument() { return document; }

public void setDocument(String document) { this.document = document; }

public String getFullName() { return fullName; }

public void setFullName(String fullName) { this.fullName = fullName; }

public String getEmail() { return email; }

public void setEmail(String email) { this.email = email; }

public String getPhone() { return phone; }

public void setPhone(String phone) { this.phone = phone; }

public String getStatus() { return status; }

public void setStatus(String status) { this.status = status; }
}
