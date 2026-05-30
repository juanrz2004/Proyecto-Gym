package com.gym.notifications.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationData {

    @Id
    private String code;

    @Column(name = "recipient_document", nullable = false)
    private String recipientDocument;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "sent_at")
    private String sentAt;

    public NotificationData() {}

    public NotificationData(String code, String recipientDocument, String recipientPhone,
                            String type, String subject, String message,
                            String status, String createdAt, String sentAt) {
        this.code = code;
        this.recipientDocument = recipientDocument;
        this.recipientPhone = recipientPhone;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getRecipientDocument() { return recipientDocument; }
    public void setRecipientDocument(String recipientDocument) { this.recipientDocument = recipientDocument; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
}
