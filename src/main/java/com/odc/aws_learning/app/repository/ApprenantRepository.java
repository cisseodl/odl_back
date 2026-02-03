package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Apprenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApprenantRepository extends JpaRepository<Apprenant,Long> {
    Page<Apprenant> findAllByActivateAndCohorteId(boolean activate, Long cohorte_id, Pageable pageable);
    Optional<Apprenant> findByUserId(Long userId);

    List<Apprenant> findByUser_IdIn(List<Long> userIds);

    @Query("SELECT DISTINCT a FROM Apprenant a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.cohorte WHERE a.user.id IN :userIds")
    List<Apprenant> findByUser_IdInWithUserAndCohorte(@Param("userIds") List<Long> userIds);
    long countByCreatedAtAfter(LocalDateTime date);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBefore(LocalDateTime date);
    
    // Méthode avec EntityGraph pour charger les relations user et cohorte de manière eager
    @EntityGraph(attributePaths = {"user", "cohorte"})
    @Query("SELECT a FROM Apprenant a")
    List<Apprenant> findAllWithUserAndCohorte();
    
    // Méthode avec JOIN FETCH pour charger les relations user et cohorte
    @Query("SELECT DISTINCT a FROM Apprenant a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.cohorte")
    List<Apprenant> findAllWithUserAndCohorteJoinFetch();
    
    // Méthode pour charger un Apprenant avec ses relations par ID
    @EntityGraph(attributePaths = {"user", "cohorte"})
    @Query("SELECT a FROM Apprenant a WHERE a.id = :id")
    Optional<Apprenant> findByIdWithUserAndCohorte(Long id);
}
