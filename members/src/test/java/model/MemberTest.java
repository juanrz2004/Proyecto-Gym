package com.gym.members.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @Test
    void shouldCreateMemberWithConstructor() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        assertEquals("doc-1", member.getDocument());
        assertEquals("name-1", member.getFullName());
        assertEquals("email-1", member.getEmail());
        assertEquals("phone-1", member.getPhone());
        assertEquals("status-1", member.getStatus());
    }

    @Test
    void shouldCreateMemberWithSetters() {
        Member member = new Member();
        member.setDocument("doc-1");
        member.setFullName("name-1");
        member.setEmail("email-1");
        member.setPhone("phone-1");
        member.setStatus("status-1");

        assertEquals("doc-1", member.getDocument());
        assertEquals("name-1", member.getFullName());
        assertEquals("email-1", member.getEmail());
        assertEquals("phone-1", member.getPhone());
        assertEquals("status-1", member.getStatus());
    }
}