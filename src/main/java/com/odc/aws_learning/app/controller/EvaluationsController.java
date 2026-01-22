package com.odc.aws_learning.app.controller;


import com.odc.aws_learning.app.service.EvaluationsService;
import com.odc.aws_learning.app.dto.EvaluationRequest;
import com.odc.aws_learning.app.dto.EvaluationSubmissionRequest;
import com.odc.aws_learning.app.dto.EvaluationCorrectionRequest;
import com.odc.aws_learning.app.dto.SatisfactionRequest;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content; // Added
import io.swagger.v3.oas.annotations.media.ExampleObject; // Added
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Added

import java.util.Optional;

@RequestMapping("/api/evaluations")
@RestController
@RequiredArgsConstructor
public class EvaluationsController {
    private final EvaluationsService evaluationsService;
    private final UserRepository userRepository;
    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()") // Added for security
    @RequestBody(
        description = "Données pour la création d'une nouvelle Évaluation",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Création d'Évaluation",
                    value = "{\n" +
                            "  \"evaluationTitle\": \"Titre de l'évaluation\",\n" +
                            "  \"questionsList\": [\n" +
                            "    {\n" +
                            "      \"title\": \"Question 1\",\n" +
                            "      \"description\": \"Description de la question 1\",\n" +
                            "      \"status\": \"ACTIVE\",\n" +
                            "      \"imagePath\": \"url_image_question1.png\",\n" +
                            "      \"type\": \"QCM\",\n" +
                            "      \"reponses\": [\n" +
                            "        {\n" +
                            "          \"title\": \"Réponse A\",\n" +
                            "          \"description\": \"Description réponse A\",\n" +
                            "          \"status\": \"ACTIVE\",\n" +
                            "          \"imagePath\": \"url_image_reponseA.png\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                )
            }
        )
    )
    public CResponse<?> saveEvaluations(@org.springframework.web.bind.annotation.RequestBody Quiz_Answer quiz_answer) {
        return evaluationsService.createEvaluation(quiz_answer);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<?> getAll() {
        return evaluationsService.getAll();
    }

    /**
     * Récupère l'examen d'un cours pour l'apprenant authentifié
     * GET /api/evaluations/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getCourseExam(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.getCourseExam(courseId, currentUser);
    }
    
    /**
     * Créer une nouvelle évaluation (instructeur)
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> createEvaluation(@org.springframework.web.bind.annotation.RequestBody EvaluationRequest request) {
        User instructor = getCurrentUser();
        if (instructor == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.createEvaluation(request, instructor);
    }
    
    /**
     * Soumettre une évaluation (apprenant)
     */
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT', 'ADMIN')")
    public CResponse<?> submitEvaluation(@org.springframework.web.bind.annotation.RequestBody EvaluationSubmissionRequest request) {
        User learner = getCurrentUser();
        if (learner == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.submitEvaluation(request, learner);
    }
    
    /**
     * Corriger une évaluation TP (instructeur)
     */
    @PostMapping("/correct")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> correctEvaluation(@org.springframework.web.bind.annotation.RequestBody EvaluationCorrectionRequest request) {
        User instructor = getCurrentUser();
        if (instructor == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.correctEvaluation(request, instructor);
    }
    
    /**
     * Récupérer les tentatives d'un apprenant pour une évaluation
     */
    @GetMapping("/{evaluationId}/attempts/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT', 'ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getAttempts(@PathVariable Long evaluationId, @PathVariable Long userId) {
        return evaluationsService.getAttemptsByEvaluationAndUser(evaluationId, userId);
    }
    
    /**
     * Récupérer les évaluations en attente de correction pour un instructeur
     */
    @GetMapping("/instructor/{instructorId}/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getPendingEvaluations(@PathVariable Long instructorId) {
        return evaluationsService.getPendingEvaluationsForInstructor(instructorId);
    }

    /**
     * Soumettre la satisfaction de l'apprenant après avoir soumis l'examen
     * POST /api/evaluations/attempts/{attemptId}/satisfaction
     */
    @PostMapping("/attempts/{attemptId}/satisfaction")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT', 'ADMIN')")
    public CResponse<?> submitSatisfaction(
            @PathVariable Long attemptId,
            @org.springframework.web.bind.annotation.RequestBody SatisfactionRequest request) {
        User learner = getCurrentUser();
        if (learner == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.submitSatisfaction(
                attemptId,
                learner,
                request.getSatisfaction(),
                request.getRating()
        );
    }

    /**
     * Récupère les résultats d'un examen (après soumission de la satisfaction)
     * GET /api/evaluations/attempts/{attemptId}/results
     */
    @GetMapping("/attempts/{attemptId}/results")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT', 'ADMIN')")
    public CResponse<?> getExamResults(@PathVariable Long attemptId) {
        User learner = getCurrentUser();
        if (learner == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.getExamResults(attemptId, learner);
    }
    
    /**
     * Supprimer une évaluation (TD ou Quiz)
     * DELETE /api/evaluations/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> deleteEvaluation(@PathVariable Long id) {
        User user = getCurrentUser();
        if (user == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return evaluationsService.deleteEvaluation(id, user);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.orElse(null);
        }
        return null;
    }
}
