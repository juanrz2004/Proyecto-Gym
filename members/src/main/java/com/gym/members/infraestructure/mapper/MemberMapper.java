package com.gym.members.infraestructure.mapper;

import com.gym.members.application.dto.MemberRequestDTO;
import com.gym.members.application.dto.MemberResponseDTO;
import com.gym.members.application.dto.MemberUpdateDTO;
import com.gym.members.domain.model.Member;
import com.gym.members.infraestructure.driver_adapters.jpa_repository.MemberData;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member toMemberFromDTO(MemberRequestDTO dto) {
        return new Member(dto.getDocument(), dto.getFullName(), dto.getEmail(), dto.getPhone(), dto.getStatus());
    }

    public Member toMemberFromUpdateDTO(MemberUpdateDTO dto) {
        return new Member(dto.getDocument(), dto.getFullName(), dto.getEmail(), dto.getPhone(), dto.getStatus());
    }

    public MemberResponseDTO toMemberResponseDTO(Member member) {
        return new MemberResponseDTO(member.getDocument(), member.getFullName(), member.getEmail(), member.getPhone(), member.getStatus());
    }

    public MemberData toData(Member member) {
        return new MemberData(member.getDocument(), member.getFullName(), member.getEmail(), member.getPhone(), member.getStatus());
    }

    public Member toDomain(MemberData data) {
        return new Member(data.getDocument(), data.getFullName(), data.getEmail(), data.getPhone(), data.getStatus());
    }
}
