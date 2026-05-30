package com.gym.notifications.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class NotificationRequestDTO {

    @NotBlank(message = "El campo codigo es obligatorio")
    private String code;

    @NotBlank(message = "El documento del destinatario es obligatorio")
    private String recipientDocument;

    @NotBlank(message = "El teléfono del destinatario es obligatorio")
    @Pattern(regexp = "\\+[1-9]\\d{7,14}",
             message = "El teléfono debe estar en formato E.164, ej: +573001234567")
    private String recipientPhone;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    @Pattern(regexp = "MEMBERSHIP_EXPIRY|PAYMENT_DUE|WELCOME|CANCELLATION|CUSTOM",
             message = "Tipo debe ser: MEMBERSHIP_EXPIRY, PAYMENT_DUE, WELCOME, CANCELLATION o CUSTOM")
    private String type;

    @NotBlank(message = "El asunto es obligatorio")
    private String subject;

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    public NotificationRequestDTO() {}

    public NotificationRequestDTO(String code, String recipientDocument, String recipientPhone,
                                  String type, String subject, String message) {
        this.code = code;
        this.recipientDocument = recipientDocument;
        this.recipientPhone = recipientPhone;
        this.type = type;
        this.subject = subject;
        this.message = message;
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
}
