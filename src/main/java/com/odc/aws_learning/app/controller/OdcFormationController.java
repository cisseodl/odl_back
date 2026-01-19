package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.OdcFormationDto;
import com.odc.aws_learning.app.dto.OdcFormationRequest;
import com.odc.aws_learning.app.service.OdcFormationService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/odc-formations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com"}, maxAge = 3600)
public class OdcFormationController {

    private final OdcFormationService odcFormationService;
    private final UserRepository userRepository;

    /**
     * Récupère toutes les formations ODC
     * GET /api/odc-formations
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<List<OdcFormationDto>>> getAllFormations() {
        try {
            List<OdcFormationDto> formations = odcFormationService.getAllFormations();
            return ResponseEntity.ok(CResponse.success(formations, "Liste des formations ODC récupérée avec succès"));
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des formations ODC", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la récupération des formations ODC: " + e.getMessage()));
        }
    }

    /**
     * Récupère une formation ODC par son ID
     * GET /api/odc-formations/{id}
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<OdcFormationDto>> getFormationById(@PathVariable Long id) {
        try {
            Optional<OdcFormationDto> formation = odcFormationService.getFormationById(id);
            if (formation.isPresent()) {
                return ResponseEntity.ok(CResponse.success(formation.get(), "Formation ODC récupérée avec succès"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CResponse.error("Formation ODC non trouvée avec l'ID: " + id));
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la formation ODC {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la récupération de la formation ODC: " + e.getMessage()));
        }
    }

    /**
     * Crée une nouvelle formation ODC
     * POST /api/odc-formations
     * Accessible uniquement aux administrateurs
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<OdcFormationDto>> createFormation(@RequestBody OdcFormationRequest request) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CResponse.error("Utilisateur non authentifié"));
            }

            OdcFormationDto createdFormation = odcFormationService.createFormation(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CResponse.success(createdFormation, "Formation ODC créée avec succès"));
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de la formation ODC", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la création de la formation ODC", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la création de la formation ODC: " + e.getMessage()));
        }
    }

    /**
     * Met à jour une formation ODC existante
     * PUT /api/odc-formations/{id}
     * Accessible uniquement aux administrateurs
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<OdcFormationDto>> updateFormation(
            @PathVariable Long id,
            @RequestBody OdcFormationRequest request) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CResponse.error("Utilisateur non authentifié"));
            }

            OdcFormationDto updatedFormation = odcFormationService.updateFormation(id, request, currentUser);
            return ResponseEntity.ok(CResponse.success(updatedFormation, "Formation ODC mise à jour avec succès"));
        } catch (RuntimeException e) {
            log.error("Erreur lors de la mise à jour de la formation ODC {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la mise à jour de la formation ODC {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la mise à jour de la formation ODC: " + e.getMessage()));
        }
    }

    /**
     * Supprime une formation ODC
     * DELETE /api/odc-formations/{id}
     * Accessible uniquement aux administrateurs
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<Void>> deleteFormation(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CResponse.error("Utilisateur non authentifié"));
            }

            odcFormationService.deleteFormation(id, currentUser);
            return ResponseEntity.ok(CResponse.success(null, "Formation ODC supprimée avec succès"));
        } catch (RuntimeException e) {
            log.error("Erreur lors de la suppression de la formation ODC {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la suppression de la formation ODC {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la suppression de la formation ODC: " + e.getMessage()));
        }
    }

    /**
     * Récupère toutes les formations créées par l'admin actuel
     * GET /api/odc-formations/my-formations
     * Accessible uniquement aux administrateurs
     */
    @GetMapping("/my-formations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<List<OdcFormationDto>>> getMyFormations() {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CResponse.error("Utilisateur non authentifié"));
            }

            List<OdcFormationDto> formations = odcFormationService.getFormationsByAdmin(currentUser);
            return ResponseEntity.ok(CResponse.success(formations, "Liste de vos formations ODC récupérée avec succès"));
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des formations ODC de l'admin", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la récupération de vos formations ODC: " + e.getMessage()));
        }
    }

    /**
     * Récupère l'utilisateur actuellement authentifié
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}
