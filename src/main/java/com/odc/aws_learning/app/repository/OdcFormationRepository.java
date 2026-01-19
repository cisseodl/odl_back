package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.OdcFormation;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OdcFormationRepository extends JpaRepository<OdcFormation, Long> {
    
    // Trouver toutes les formations créées par un admin spécifique
    List<OdcFormation> findByAdmin(User admin);
    
    // Trouver toutes les formations actives
    List<OdcFormation> findByActivateTrue();
    
    // Trouver une formation par ID et admin
    Optional<OdcFormation> findByIdAndAdmin(Long id, User admin);
}
