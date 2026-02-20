package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.DashboardService;
import com.odc.aws_learning.app.wrapper.DashboardStatsDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository; // Added
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Added
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository; // Added

    @GetMapping("/student")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT')")
    public CResponse<DashboardStatsDTO> getDashboardForStudent() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        DashboardStatsDTO stats = dashboardService.getDashboardForUser(currentUser);
        return CResponse.success(stats, "Statistiques du tableau de bord étudiant");
    }

    @GetMapping("/instructor")
    @PreAuthorize("isAuthenticated()") // Permettre à tous les utilisateurs authentifiés, on vérifie le profil après
    public CResponse<DashboardStatsDTO> getDashboardForInstructor() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        // Vérifier que l'utilisateur a un profil Instructor
        if (currentUser.getInstructor() == null) {
            return CResponse.error("L'utilisateur n'a pas de profil instructeur. Veuillez créer un profil instructeur d'abord.");
        }
        // Retourner les stats de l'instructeur
        DashboardStatsDTO stats = dashboardService.getInstructorStats(currentUser);
        return CResponse.success(stats, "Statistiques du tableau de bord instructeur");
    }

    /**
     * Récupère les statistiques publiques pour la page d'accueil
     * GET /api/dashboard/public-stats
     * Endpoint public (pas d'authentification requise)
     */
    @GetMapping("/public-stats")
    public CResponse<?> getPublicStats() {
        return dashboardService.getPublicStats();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        String email = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof String && !"anonymousUser".equals(authentication.getPrincipal())) {
            email = (String) authentication.getPrincipal();
        }
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmailWithInstructor(email).orElse(null);
        }
        return null;
    }
}
