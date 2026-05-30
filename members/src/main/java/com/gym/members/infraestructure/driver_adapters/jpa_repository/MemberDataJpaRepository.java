package com.gym.members.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberDataJpaRepository extends JpaRepository<MemberData, String> {
    Optional<MemberData> findByDocument(String document);
    boolean existsByDocument(String document);
    void deleteByDocument(String document);
}
