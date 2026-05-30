package com.gym.members.domain.usecase;

import com.gym.members.domain.model.Member;
import com.gym.members.domain.model.gateway.MemberGateway;

import java.util.List;
import java.util.NoSuchElementException;

public class MemberUseCase {
    private final MemberGateway gateway;

    public MemberUseCase(MemberGateway gateway) {
        this.gateway = gateway;
    }

    public Member saveMember(Member member) {
        String document = member.getDocument();
        if (gateway.existsByDocument(document)) {
            throw new IllegalArgumentException("Ya existe un registro con document: " + document);
        }
        return gateway.save(member);
    }

    public Member getMemberByDocument(String document) {
        return gateway.findByDocument(document)
                .orElseThrow(() -> new NoSuchElementException("No existe registro con document: " + document));
    }

    public List<Member> listMembers() {
        return gateway.findAll();
    }

    public Member updateMember(String document, Member member) {
        if (!gateway.existsByDocument(document)) {
            throw new NoSuchElementException("No existe registro con document: " + document);
        }
        member.setDocument(document);
        return gateway.save(member);
    }

    public void deleteMemberByDocument(String document) {
        if (!gateway.existsByDocument(document)) {
            throw new NoSuchElementException("No existe registro con document: " + document);
        }
        gateway.deleteByDocument(document);
    }
}
