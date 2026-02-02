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

    @GetMapping("/course/{courseId}")
    // Endpoint public : permet la consultation des modules sans authentification
    // Les modules et leçons sont visibles pour tous, mais certaines actions nécessitent l'authentification
    public CResponse<?> getModulesByCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        return moduleService.getModulesByCourse(courseId, currentUser);
    }


}
