package com.gym.members.application.dto;

    import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public class MemberUpdateDTO {
        @NotBlank(message = "El campo documento es obligatorio")

    private String document;

    @NotBlank(message = "El campo nombre completo es obligatorio")

    private String fullName;

    @NotBlank(message = "El campo email es obligatorio")

    private String email;

    @NotBlank(message = "El campo telefono es obligatorio")

    private String phone;

    @NotBlank(message = "El campo estado es obligatorio")

    private String status;

        public MemberUpdateDTO() {}

        public MemberUpdateDTO(String document, String fullName, String email, String phone, String status) {
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
