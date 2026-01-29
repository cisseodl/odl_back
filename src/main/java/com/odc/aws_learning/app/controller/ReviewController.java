package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.ReviewRequest;
import com.odc.aws_learning.app.dto.ReviewResponseDto;
import com.odc.aws_learning.app.service.ReviewService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com"}, maxAge = 3600)
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Récupère tous les avis pour un cours donné.
     * Accessible publiquement, car les avis sont généralement visibles par tous.
     * @param courseId L'ID du cours.
     * @return Une liste de ReviewResponseDto.
     */
    @GetMapping("/course/{courseId}")
    public CResponse<List<ReviewResponseDto>> getReviewsByCourse(@PathVariable Long courseId) {
        try {
            return reviewService.getReviewsByCourse(courseId);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des avis pour le cours {}: {}", courseId, e.getMessage(), e);
            return CResponse.error("Erreur lors de la récupération des avis: " + e.getMessage());
        }
    }

    /**
     * Ajoute un avis pour un cours donné.
     * Seuls les utilisateurs inscrits au cours peuvent donner un avis.
     * @param courseId L'ID du cours.
     * @param request Les données de l'avis (rating, comment).
     * @param currentUser L'utilisateur authentifié.
     * @return Un ReviewResponseDto.
     */
    @PostMapping("/add/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT')")
    public ResponseEntity<CResponse<ReviewResponseDto>> addReview(
            @PathVariable Long courseId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<ReviewResponseDto> response = reviewService.addReview(courseId, currentUser, request.getRating(), request.getComment());
        return ResponseEntity.ok(response);
    }
}