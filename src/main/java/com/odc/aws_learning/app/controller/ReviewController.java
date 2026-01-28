package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.ReviewResponseDto;
import com.odc.aws_learning.app.service.ReviewService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    // TODO: Ajouter des endpoints pour ajouter, modifier, supprimer des avis si nécessaire
    // Ces endpoints devraient être protégés par @PreAuthorize pour les utilisateurs authentifiés
    // Par exemple:
    // @PostMapping("/add/{courseId}")
    // @PreAuthorize("hasAnyRole('USER', 'APPRENANT')")
    // public CResponse<ReviewDto> addReview(@PathVariable Long courseId, @RequestBody ReviewCreationRequest request) {
    //     // ...
    // }
}