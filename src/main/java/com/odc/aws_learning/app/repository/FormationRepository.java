package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    /**
     * Trouve toutes les formations d'une catégorie
     */
    List<Formation> findByCategorieId(Long categorieId);
    
    /**
     * Trouve toutes les formations actives d'une catégorie
     */
    List<Formation> findByCategorieIdAndActivateTrue(Long categorieId);
    
    /**
     * Trouve une formation par son titre (utile pour éviter les doublons)
     */
    Optional<Formation> findByTitle(String title);
    
    /**
     * Trouve toutes les formations actives
     */
    List<Formation> findByActivateTrue();
}

