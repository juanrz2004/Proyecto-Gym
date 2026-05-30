package com.gym.members.infraestructure.driver_adapters.jpa_repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberDataTest {
    @Test
    void shouldCreateMemberDataWithConstructor() {
        MemberData data = new MemberData("doc-1", "name-1", "email-1", "phone-1", "status-1");
        assertEquals("doc-1", data.getDocument());
        assertEquals("name-1", data.getFullName());
        assertEquals("email-1", data.getEmail());
        assertEquals("phone-1", data.getPhone());
        assertEquals("status-1", data.getStatus());
    }

    @Test
    void shouldCreateMemberDataWithSetters() {
        MemberData data = new MemberData();
        data.setDocument("doc-1");
        data.setFullName("name-1");
        data.setEmail("email-1");
        data.setPhone("phone-1");
        data.setStatus("status-1");

        assertEquals("doc-1", data.getDocument());
        assertEquals("name-1", data.getFullName());
        assertEquals("email-1", data.getEmail());
        assertEquals("phone-1", data.getPhone());
        assertEquals("status-1", data.getStatus());
    }
}