package com.gym.notifications.application.dto;

public class NotificationResponseDTO {
    private String code;
    private String recipientDocument;
    private String recipientPhone;
    private String type;
    private String subject;
    private String message;
    private String status;
    private String createdAt;
    private String sentAt;

    public NotificationResponseDTO() {}

    public NotificationResponseDTO(String code, String recipientDocument, String recipientPhone,
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
