package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.FormationRequest;
import com.odc.aws_learning.app.entity.Formation;
import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.repository.FormationRepository;
import com.odc.aws_learning.app.repository.CategorieRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des Formations
 * Hiérarchie : Catégorie -> Formation -> Cours -> Module -> Leçon
 */
@Service
@Transactional
public class FormationService {

    private final FormationRepository formationRepository;
    private final CategorieRepository categorieRepository;

    public FormationService(FormationRepository formationRepository, CategorieRepository categorieRepository) {
        this.formationRepository = formationRepository;
        this.categorieRepository = categorieRepository;
    }

    /**
     * Récupère toutes les formations
     */
    @Transactional(readOnly = true)
    public CResponse<List<Formation>> getAllFormations() {
        try {
            List<Formation> formations = formationRepository.findAll();
            return CResponse.success(formations, "Formations récupérées avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des formations: " + e.getMessage());
        }
    }

    /**
     * Récupère une formation par son ID
     */
    @Transactional(readOnly = true)
    public CResponse<Formation> getFormationById(Long id) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isEmpty()) {
                return CResponse.error("Formation non trouvée avec l'ID: " + id);
            }
            return CResponse.success(formationOptional.get(), "Formation récupérée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération de la formation: " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les formations d'une catégorie
     */
    @Transactional(readOnly = true)
    public CResponse<List<Formation>> getFormationsByCategorieId(Long categorieId) {
        try {
            List<Formation> formations = formationRepository.findByCategorieId(categorieId);
            return CResponse.success(formations, "Formations récupérées avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des formations: " + e.getMessage());
        }
    }

    /**
     * Crée une nouvelle formation
     */
    public CResponse<Formation> createFormation(FormationRequest request) {
        try {
            // Vérifier que la catégorie existe
            Optional<Categorie> categorieOptional = categorieRepository.findById(request.getCategorieId());
            if (categorieOptional.isEmpty()) {
                return CResponse.error("Catégorie non trouvée avec l'ID: " + request.getCategorieId());
            }

            Formation formation = new Formation();
            formation.setTitle(request.getTitle());
            formation.setDescription(request.getDescription());
            formation.setImagePath(request.getImagePath());
            formation.setCategorie(categorieOptional.get());
            formation.setActivate(request.getActivate() != null ? request.getActivate() : true);

            Formation savedFormation = formationRepository.save(formation);
            return CResponse.success(savedFormation, "Formation créée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la création de la formation: " + e.getMessage());
        }
    }

    /**
     * Met à jour une formation existante
     */
    public CResponse<Formation> updateFormation(Long id, FormationRequest request) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isEmpty()) {
                return CResponse.error("Formation non trouvée avec l'ID: " + id);
            }

            Formation formation = formationOptional.get();
            formation.setTitle(request.getTitle());
            formation.setDescription(request.getDescription());
            
            if (request.getImagePath() != null) {
                formation.setImagePath(request.getImagePath());
            }

            // Mettre à jour la catégorie si elle change
            if (request.getCategorieId() != null && 
                (formation.getCategorie() == null || !formation.getCategorie().getId().equals(request.getCategorieId()))) {
                Optional<Categorie> categorieOptional = categorieRepository.findById(request.getCategorieId());
                if (categorieOptional.isEmpty()) {
                    return CResponse.error("Catégorie non trouvée avec l'ID: " + request.getCategorieId());
                }
                formation.setCategorie(categorieOptional.get());
            }

            if (request.getActivate() != null) {
                formation.setActivate(request.getActivate());
            }

            Formation updatedFormation = formationRepository.save(formation);
            return CResponse.success(updatedFormation, "Formation mise à jour avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la mise à jour de la formation: " + e.getMessage());
        }
    }

    /**
     * Supprime une formation
     */
    public CResponse<Void> deleteFormation(Long id) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isEmpty()) {
                return CResponse.error("Formation non trouvée avec l'ID: " + id);
            }

            Formation formation = formationOptional.get();
            
            // Vérifier qu'il n'y a pas de cours associés
            if (formation.getCourses() != null && !formation.getCourses().isEmpty()) {
                return CResponse.error("Impossible de supprimer la formation. Il existe des cours associés à cette formation.");
            }

            formationRepository.delete(formation);
            return CResponse.success(null, "Formation supprimée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la suppression de la formation: " + e.getMessage());
        }
    }
}

