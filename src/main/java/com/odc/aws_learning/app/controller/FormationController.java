package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.FormationRequest;
import com.odc.aws_learning.app.entity.Formation;
import com.odc.aws_learning.app.service.FormationService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des Formations
 * Hiérarchie : Catégorie -> Formation -> Cours -> Module -> Leçon
 */
@RestController
@RequestMapping("/api/formations")
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com"}, maxAge = 3600)
public class FormationController {

    private final FormationService formationService;

    public FormationController(FormationService formationService) {
        this.formationService = formationService;
    }

    /**
     * GET /api/formations
     * Récupère toutes les formations
     */
    @GetMapping
    public CResponse<List<Formation>> getAllFormations() {
        return formationService.getAllFormations();
    }

    /**
     * GET /api/formations/{id}
     * Récupère une formation par son ID
     */
    @GetMapping("/{id}")
    public CResponse<Formation> getFormationById(@PathVariable Long id) {
        return formationService.getFormationById(id);
    }

    /**
     * GET /api/formations/categorie/{categorieId}
     * Récupère toutes les formations d'une catégorie
     */
    @GetMapping("/categorie/{categorieId}")
    public CResponse<List<Formation>> getFormationsByCategorieId(@PathVariable Long categorieId) {
        return formationService.getFormationsByCategorieId(categorieId);
    }

    /**
     * POST /api/formations
     * Crée une nouvelle formation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<Formation> createFormation(@RequestBody FormationRequest request) {
        return formationService.createFormation(request);
    }

    /**
     * PUT /api/formations/{id}
     * Met à jour une formation existante
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<Formation> updateFormation(@PathVariable Long id, @RequestBody FormationRequest request) {
        return formationService.updateFormation(id, request);
    }

    /**
     * DELETE /api/formations/{id}
     * Supprime une formation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CResponse<Void> deleteFormation(@PathVariable Long id) {
        return formationService.deleteFormation(id);
    }
}

