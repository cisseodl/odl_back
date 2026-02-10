package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.LabDefinitionRequest;
import com.odc.aws_learning.app.entity.LabDefinition;
import com.odc.aws_learning.app.entity.LabSession;
import com.odc.aws_learning.app.service.LabService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des Labs (sessions pratiques).
 * Endpoints : /api/labs
 */
@RestController
@RequestMapping("/api/labs")
@RequiredArgsConstructor
public class LabController {
    
    private final LabService labService;
    private final UserRepository userRepository;
    
    /**
     * Liste tous les labs disponibles.
     * GET /api/labs/
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN, INSTRUCTOR)
     */
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<List<LabDefinition>>> getAllLabs() {
        CResponse<List<LabDefinition>> response = labService.getAllLabs();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupère un lab par son ID.
     * GET /api/labs/{id}
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabDefinition>> getLabById(@PathVariable Long id) {
        CResponse<LabDefinition> response = labService.getLabById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Crée un nouveau lab.
     * POST /api/labs/
     * Accessible aux ADMIN et INSTRUCTOR
     */
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabDefinition>> createLab(@Valid @RequestBody LabDefinitionRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<LabDefinition> response = labService.createLab(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Met à jour un lab existant.
     * PUT /api/labs/{id}
     * Accessible aux ADMIN et INSTRUCTOR
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabDefinition>> updateLab(
            @PathVariable Long id,
            @Valid @RequestBody LabDefinitionRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<LabDefinition> response = labService.updateLab(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Supprime un lab.
     * DELETE /api/labs/{id}
     * Accessible aux ADMIN et INSTRUCTOR
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<Void>> deleteLab(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<Void> response = labService.deleteLab(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lance un lab pour l'utilisateur connecté.
     * POST /api/labs/start/{labId}
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN, INSTRUCTOR)
     */
    @PostMapping("/start/{labId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabSession>> startLab(@PathVariable Long labId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        CResponse<LabSession> response = labService.startLab(labId, currentUser.getEmail());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Arrête un lab.
     * POST /api/labs/stop/{sessionId}
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN, INSTRUCTOR)
     */
    @PostMapping("/stop/{sessionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabSession>> stopLab(@PathVariable Long sessionId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        // Vérifier que la session appartient à l'utilisateur connecté
        // (Cette vérification pourrait être déplacée dans le service pour plus de sécurité)
        CResponse<LabSession> response = labService.stopLab(sessionId);
        
        // Vérification supplémentaire : s'assurer que l'utilisateur est propriétaire de la session
        if (response.isSuccess() && response.getData() != null) {
            LabSession session = response.getData();
            if (!session.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.ok(CResponse.error("Vous n'êtes pas autorisé à arrêter cette session"));
            }
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Soumet le résultat d'un lab.
     * POST /api/labs/submit/{sessionId}
     * Body optionnel : { "reportUrl": "http://..." }
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN, INSTRUCTOR)
     */
    @PostMapping("/submit/{sessionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabSession>> submitLab(
            @PathVariable Long sessionId,
            @RequestBody(required = false) SubmitLabRequest request) {
        
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        String reportUrl = (request != null) ? request.getReportUrl() : null;
        CResponse<LabSession> response = labService.submitLab(sessionId, reportUrl);
        
        // Vérification supplémentaire : s'assurer que l'utilisateur est propriétaire de la session
        if (response.isSuccess() && response.getData() != null) {
            LabSession session = response.getData();
            if (!session.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.ok(CResponse.error("Vous n'êtes pas autorisé à soumettre cette session"));
            }
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Soumet un rapport de lab sans session RUNNING (fichier ou texte selon instructions instructeur).
     * POST /api/labs/submit-report/{labId}
     * Body : { "reportUrl": "https://..." } (URL après upload du fichier ou d'un .txt généré depuis le texte)
     */
    @PostMapping("/submit-report/{labId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<LabSession>> submitLabReport(
            @PathVariable Long labId,
            @RequestBody(required = false) SubmitLabRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        String reportUrl = (request != null) ? request.getReportUrl() : null;
        CResponse<LabSession> response = labService.submitLabReport(labId, reportUrl, currentUser);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupère toutes les sessions de l'utilisateur connecté.
     * GET /api/labs/my-sessions
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN, INSTRUCTOR)
     */
    @GetMapping("/my-sessions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<List<LabSession>>> getMySessions() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        CResponse<List<LabSession>> response = labService.getUserSessions(currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupère l'utilisateur actuellement authentifié
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.orElse(null);
        }
        return null;
    }
    
    /**
     * DTO pour la requête de soumission de lab
     */
    public static class SubmitLabRequest {
        private String reportUrl;
        
        public String getReportUrl() {
            return reportUrl;
        }
        
        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }
    }
}
