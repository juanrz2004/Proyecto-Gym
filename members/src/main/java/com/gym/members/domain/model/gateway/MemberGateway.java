package com.gym.members.domain.model.gateway;

import com.gym.members.domain.model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberGateway {
    Member save(Member member);
    Optional<Member> findByDocument(String document);
    List<Member> findAll();
    void deleteByDocument(String document);
    boolean existsByDocument(String document);
}
