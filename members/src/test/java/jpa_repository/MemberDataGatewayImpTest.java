package com.gym.members.infraestructure.driver_adapters.jpa_repository;

import com.gym.members.domain.model.Member;
import com.gym.members.infraestructure.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberDataGatewayImpTest {

    private MemberDataJpaRepository repository;
    private MemberMapper mapper;
    private MemberDataGatewayImp gateway;

    @BeforeEach
    void setUp() {
        repository = mock(MemberDataJpaRepository.class);
        mapper = mock(MemberMapper.class);
        gateway = new MemberDataGatewayImp(repository, mapper);
    }

    @Test
    void shouldSaveMember() {
        Member domain = mock(Member.class);
        MemberData data = mock(MemberData.class);

        when(mapper.toData(domain)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toDomain(data)).thenReturn(domain);

        Member result = gateway.save(domain);

        assertNotNull(result);
        verify(repository).save(data);
    }

    @Test
    void shouldFindByDocument() {
        String document = "123456";
        Member domain = mock(Member.class);
        MemberData data = mock(MemberData.class);

        when(repository.findByDocument(document)).thenReturn(Optional.of(data));
        when(mapper.toDomain(data)).thenReturn(domain);

        Optional<Member> result = gateway.findByDocument(document);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void shouldReturnEmptyWhenDocumentNotFound() {
        when(repository.findByDocument("999")).thenReturn(Optional.empty());

        Optional<Member> result = gateway.findByDocument("999");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllMembers() {
        Member domain = mock(Member.class);
        MemberData data = mock(MemberData.class);

        when(repository.findAll()).thenReturn(List.of(data));
        when(mapper.toDomain(data)).thenReturn(domain);

        List<Member> result = gateway.findAll();

        assertEquals(1, result.size());
        assertEquals(domain, result.get(0));
    }

    @Test
    void shouldDeleteByDocument() {
        String document = "123456";
        doNothing().when(repository).deleteByDocument(document);

        gateway.deleteByDocument(document);

        verify(repository).deleteByDocument(document);
    }

    @Test
    void shouldReturnTrueWhenDocumentExists() {
        when(repository.existsByDocument("123456")).thenReturn(true);

        assertTrue(gateway.existsByDocument("123456"));
    }

    @Test
    void shouldReturnFalseWhenDocumentNotExists() {
        when(repository.existsByDocument("999")).thenReturn(false);

        assertFalse(gateway.existsByDocument("999"));
    }
}
