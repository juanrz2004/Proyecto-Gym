package com.gym.members.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemberResponseDTOTest {

    @Test
    void shouldCreateWithConstructor() {
        MemberResponseDTO dto = new MemberResponseDTO("doc-1", "name-1", "email-1", "phone-1", "status-1");
        assertEquals("doc-1", dto.getDocument());
        assertEquals("name-1", dto.getFullName());
        assertEquals("email-1", dto.getEmail());
        assertEquals("phone-1", dto.getPhone());
        assertEquals("status-1", dto.getStatus());
    }

    @Test
    void shouldCreateWithSetters() {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setDocument("doc-1");
        dto.setFullName("name-1");
        dto.setEmail("email-1");
        dto.setPhone("phone-1");
        dto.setStatus("status-1");

        assertEquals("doc-1", dto.getDocument());
        assertEquals("name-1", dto.getFullName());
        assertEquals("email-1", dto.getEmail());
        assertEquals("phone-1", dto.getPhone());
        assertEquals("status-1", dto.getStatus());
    }
}