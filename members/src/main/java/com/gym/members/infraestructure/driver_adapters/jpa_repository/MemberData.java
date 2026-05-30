package com.gym.members.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class MemberData {
    @Id

private String document;

private String fullName;

private String email;

private String phone;

private String status;

    public MemberData() {}

    public MemberData(String document, String fullName, String email, String phone, String status) {
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
