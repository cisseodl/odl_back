package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.ModuleService;
import com.odc.aws_learning.app.wrapper.ModuleAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;


@RequestMapping("/modules")
@RestController

public class ModuleController {
    private final ModuleService moduleService;
    private final UserRepository userRepository;
    
    public ModuleController(ModuleService moduleService, UserRepository userRepository) {
        this.moduleService = moduleService;
        this.userRepository = userRepository;
    }
    
    /**
     * Récupère l'utilisateur actuellement authentifié
     * Supporte à la fois UserDetails et String (email) comme principal (pour JWT)
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = null;
            
            // Si le principal est un UserDetails, récupérer l'email
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                email = userDetails.getUsername();
            }
            // Si le principal est une String (cas JWT), utiliser directement
            else if (authentication.getPrincipal() instanceof String) {
                String principal = (String) authentication.getPrincipal();
                // Vérifier que ce n'est pas "anonymousUser"
                if (!"anonymousUser".equals(principal)) {
                    email = principal;
                }
            }
            
            // Si on a un email, charger l'utilisateur
            if (email != null && !email.isEmpty()) {
                // Charger le User avec la relation instructor pour que getModulesByCourse reconnaisse les instructeurs
                Optional<User> userOptional = userRepository.findByEmailWithInstructor(email);
                return userOptional.orElse(null);
            }
        }
        return null;
    }
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> saveModule(@RequestPart("module") ModuleAndCoursePayload moduleAndCoursePayload,
                                    @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {
        try {
            if (moduleAndCoursePayload.getCourseId() == null) {
                return CResponse.error("Le courseId est requis.");
            }
            
            if (moduleAndCoursePayload.getModules() == null || moduleAndCoursePayload.getModules().isEmpty()) {
                return CResponse.error("Au moins un module est requis.");
            }
            
            CResponse<?> response = moduleService.saveModule(moduleAndCoursePayload, pdfFile);
            return response;
        } catch (Exception e) {
            System.err.println("ERREUR dans saveModule: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la sauvegarde du module: " + e.getMessage());
        }
    }

    // Endpoint public : permet la consultation des modules sans authentification
    // Les modules et leçons sont visibles pour tous, mais certaines actions nécessitent l'authentification

    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<CResponse<?>> getModulesByCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        CResponse<?> response = moduleService.getModulesByCourse(courseId, currentUser);
    
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // Retourne 200 OK avec la réponse du service
        } else {
            // Le message d'erreur du service indique un problème (par exemple, non inscrit)
            // On retourne un statut 403 Forbidden (Accès Refusé)
            // Le corps contiendra le CResponse avec le message d'erreur
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); 
        }
    }

}
