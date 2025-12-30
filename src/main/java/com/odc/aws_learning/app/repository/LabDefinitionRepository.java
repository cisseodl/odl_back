package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.LabDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des définitions de labs.
 */
@Repository
public interface LabDefinitionRepository extends JpaRepository<LabDefinition, Long> {
    
    /**
     * Récupère toutes les définitions de labs actives.
     * @return Liste des labs disponibles
     */
    List<LabDefinition> findByActivateTrue();
}
