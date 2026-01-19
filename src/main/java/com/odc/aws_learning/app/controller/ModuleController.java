package com.odc.aws_learning.app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> saveModule(@RequestParam("module") String moduleAndCoursePayloadsString,
                                    @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) throws JsonProcessingException {
        try {
            System.out.println("=== RECEPTION DU PAYLOAD MODULES ===");
            System.out.println("Payload JSON reçu: " + moduleAndCoursePayloadsString);
            
            if (moduleAndCoursePayloadsString == null || moduleAndCoursePayloadsString.trim().isEmpty()) {
                System.err.println("ERREUR: Le payload module est null ou vide!");
                return CResponse.error("Le payload module est requis.");
            }
            
            ModuleAndCoursePayload moduleAndCoursePayload = new ObjectMapper().readValue(moduleAndCoursePayloadsString, ModuleAndCoursePayload.class);
            
            System.out.println("CourseId: " + moduleAndCoursePayload.getCourseId());
            System.out.println("CourseType: " + (moduleAndCoursePayload.getCourseType() != null ? moduleAndCoursePayload.getCourseType() : "NULL"));
            System.out.println("Nombre de modules: " + (moduleAndCoursePayload.getModules() != null ? moduleAndCoursePayload.getModules().size() : 0));
            
            if (moduleAndCoursePayload.getCourseId() == null) {
                System.err.println("ERREUR: courseId est null!");
                return CResponse.error("Le courseId est requis.");
            }
            
            if (moduleAndCoursePayload.getModules() == null || moduleAndCoursePayload.getModules().isEmpty()) {
                System.err.println("ERREUR: Aucun module dans le payload!");
                return CResponse.error("Au moins un module est requis.");
            }
            
            if (moduleAndCoursePayload.getModules() != null) {
                for (int i = 0; i < moduleAndCoursePayload.getModules().size(); i++) {
                    var m = moduleAndCoursePayload.getModules().get(i);
                    System.out.println("Module " + i + ": " + m.getTitle() + ", Order: " + m.getModuleOrder() + ", Leçons: " + (m.getLessons() != null ? m.getLessons().size() : 0));
                    if (m.getLessons() != null) {
                        for (int j = 0; j < m.getLessons().size(); j++) {
                            var l = m.getLessons().get(j);
                            System.out.println("  Leçon " + j + ": " + l.getTitle() + ", Type: " + l.getType() + ", Order: " + l.getLessonOrder() + ", ContentUrl: " + l.getContentUrl());
                        }
                    }
                }
            }
            
            System.out.println("Appel de moduleService.saveModule...");
            CResponse<?> response = moduleService.saveModule(moduleAndCoursePayload, pdfFile);
            System.out.println("Réponse du service: " + (response != null ? response.getMessage() : "NULL"));
            return response;
        } catch (Exception e) {
            System.err.println("ERREUR dans saveModule: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la sauvegarde du module: " + e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<?> getModulesByCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        return moduleService.getModulesByCourse(courseId, currentUser);
    }


}
