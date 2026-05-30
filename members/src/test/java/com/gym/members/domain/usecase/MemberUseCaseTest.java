package com.gym.members.domain.usecase;

import com.gym.members.domain.model.Member;
import com.gym.members.domain.model.gateway.MemberGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberUseCaseTest {
    private MemberGateway gateway;
    private MemberUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(MemberGateway.class);
        useCase = new MemberUseCase(gateway);
    }

    @Test
    void shouldSaveMember() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        when(gateway.existsByDocument("doc-1")).thenReturn(false);
        when(gateway.save(member)).thenReturn(member);

        Member result = useCase.saveMember(member);
        assertEquals("doc-1", result.getDocument());
    }

    @Test
    void shouldThrowWhenSaveDuplicateDocument() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        when(gateway.existsByDocument("doc-1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.saveMember(member));
    }

    @Test
    void shouldGetMemberByDocument() {
        Member member = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        when(gateway.findByDocument("doc-1")).thenReturn(Optional.of(member));

        Member result = useCase.getMemberByDocument("doc-1");
        assertEquals("doc-1", result.getDocument());
    }

    @Test
    void shouldThrowWhenGetMemberNotFound() {
        when(gateway.findByDocument("not-exists")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> useCase.getMemberByDocument("not-exists"));
    }

    @Test
    void shouldListMembers() {
        Member member1 = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        Member member2 = new Member("doc-2", "name-2", "email-2", "phone-2", "status-2");
        when(gateway.findAll()).thenReturn(List.of(member1, member2));

        List<Member> result = useCase.listMembers();
        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateMember() {
        Member member = new Member("doc-1", "name-updated", "email-updated", "phone-updated", "status-updated");
        when(gateway.existsByDocument("doc-1")).thenReturn(true);
        when(gateway.save(member)).thenReturn(member);

        Member result = useCase.updateMember("doc-1", member);
        assertEquals("doc-1", result.getDocument());
    }

    @Test
    void shouldThrowWhenUpdateMemberNotFound() {
        Member member = new Member("not-exists", "name", "email", "phone", "status");
        when(gateway.existsByDocument("not-exists")).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> useCase.updateMember("not-exists", member));
    }

    @Test
    void shouldDeleteMember() {
        when(gateway.existsByDocument("doc-1")).thenReturn(true);
        doNothing().when(gateway).deleteByDocument("doc-1");

        assertDoesNotThrow(() -> useCase.deleteMemberByDocument("doc-1"));
        verify(gateway).deleteByDocument("doc-1");
    }

    @Test
    void shouldThrowWhenDeleteMemberNotFound() {
        when(gateway.existsByDocument("not-exists")).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> useCase.deleteMemberByDocument("not-exists"));
    }
}