package com.gym.members.infraestructure.driver_adapters.jpa_repository;

import com.gym.members.domain.model.Member;
import com.gym.members.domain.model.gateway.MemberGateway;
import com.gym.members.infraestructure.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDataGatewayImp implements MemberGateway {
    private final MemberDataJpaRepository repository;
    private final MemberMapper mapper;

    public MemberDataGatewayImp(MemberDataJpaRepository repository, MemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Member save(Member member) {
        return mapper.toDomain(repository.save(mapper.toData(member)));
    }

    @Override
    public Optional<Member> findByDocument(String document) {
        return repository.findByDocument(document).map(mapper::toDomain);
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByDocument(String document) {
        repository.deleteByDocument(document);
    }

    @Override
    public boolean existsByDocument(String document) {
        return repository.existsByDocument(document);
    }
}
