package com.gym.members.infraestructure.mapper;

import com.gym.members.application.dto.MemberRequestDTO;
import com.gym.members.application.dto.MemberResponseDTO;
import com.gym.members.application.dto.MemberUpdateDTO;
import com.gym.members.domain.model.Member;
import com.gym.members.infraestructure.driver_adapters.jpa_repository.MemberData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberMapperTest {
    private final MemberMapper mapper = new MemberMapper();

    @Test
    void shouldMapFromRequestDTO() {
        MemberRequestDTO dto = new MemberRequestDTO("doc-1", "name-1", "email-1", "phone-1", "status-1");
        Member member = mapper.toMemberFromDTO(dto);
        assertEquals("doc-1", member.getDocument());
        assertEquals("name-1", member.getFullName());
    }

    @Test
    void shouldMapFromUpdateDTO() {
        MemberUpdateDTO dto = new MemberUpdateDTO("doc-1", "name-1", "email-1", "phone-1", "status-1");
        Member member = mapper.toMemberFromUpdateDTO(dto);
        assertEquals("doc-1", member.getDocument());
        assertEquals("name-1", member.getFullName());
    }

    @Test
    void shouldMapToResponseDTO() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        MemberResponseDTO dto = mapper.toMemberResponseDTO(member);
        assertEquals("doc-1", dto.getDocument());
        assertEquals("name-1", dto.getFullName());
    }

    @Test
    void shouldMapToData() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        MemberData data = mapper.toData(member);
        assertEquals("doc-1", data.getDocument());
        assertEquals("name-1", data.getFullName());
    }

    @Test
    void shouldMapToDomain() {
        MemberData data = new MemberData("doc-1", "name-1", "email-1", "phone-1", "status-1");
        Member member = mapper.toDomain(data);
        assertEquals("doc-1", member.getDocument());
        assertEquals("name-1", member.getFullName());
    }
}