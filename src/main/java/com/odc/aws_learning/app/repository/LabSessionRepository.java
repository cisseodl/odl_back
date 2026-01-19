package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.LabSession;
import com.odc.aws_learning.app.entity.LabSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des sessions de labs.
 */
@Repository
public interface LabSessionRepository extends JpaRepository<LabSession, Long> {
    
    /**
     * Trouve toutes les sessions d'un utilisateur.
     * @param userId ID de l'utilisateur
     * @return Liste des sessions de l'utilisateur
     */
    List<LabSession> findByUserId(Long userId);
    
    /**
     * Trouve toutes les sessions d'un utilisateur pour un lab spécifique.
     * @param userId ID de l'utilisateur
     * @param labDefinitionId ID de la définition du lab
     * @return Liste des sessions
     */
    List<LabSession> findByUserIdAndLabDefinitionId(Long userId, Long labDefinitionId);
    
    /**
     * Trouve une session active (RUNNING ou STARTING) pour un utilisateur et un lab.
     * @param userId ID de l'utilisateur
     * @param labDefinitionId ID de la définition du lab
     * @param statuses Liste des statuts à rechercher (RUNNING, STARTING)
     * @return Session active si elle existe
     */
    Optional<LabSession> findFirstByUserIdAndLabDefinitionIdAndStatusIn(
            Long userId, 
            Long labDefinitionId, 
            List<LabSessionStatus> statuses
    );
    
    /**
     * Trouve toutes les sessions avec un statut spécifique.
     * @param status Statut à rechercher
     * @return Liste des sessions avec ce statut
     */
    List<LabSession> findByStatus(LabSessionStatus status);
    
    /**
     * Trouve toutes les sessions d'un lab avec des statuts spécifiques.
     * @param labDefinitionId ID de la définition du lab
     * @param statuses Liste des statuts à rechercher
     * @return Liste des sessions
     */
    List<LabSession> findByLabDefinitionIdAndStatusIn(Long labDefinitionId, List<LabSessionStatus> statuses);
    
    /**
     * Trouve toutes les sessions avec des statuts spécifiques.
     * @param statuses Liste des statuts à rechercher
     * @return Liste des sessions avec ces statuts
     */
    List<LabSession> findByStatusIn(List<LabSessionStatus> statuses);
}
