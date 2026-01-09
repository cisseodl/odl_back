package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Apprenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ApprenantRepository extends JpaRepository<Apprenant,Long> {
    Page<Apprenant> findAllByActivateAndCohorteId(boolean activate, Long cohorte_id, Pageable pageable);
    Optional<Apprenant> findByUserId(Long userId);
    long countByCreatedAtAfter(LocalDateTime date);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBefore(LocalDateTime date);
}
