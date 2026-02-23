package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.entity.EvaluationAttempt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO pour afficher une tentative d'évaluation côté instructeur (titre évaluation, nom apprenant, fichier/texte soumis).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationAttemptForInstructorDto {
    private Long id;
    private Long evaluationId;
    private String evaluationTitle;
    private Long userId;
    private String learnerName;
    private String learnerEmail;
    private String status;
    private Double score;
    private String submittedFileUrl;
    private String submittedText;
    private String instructorFeedback;
    private Instant correctedAt;
    private LocalDateTime createdAt;

    public static EvaluationAttemptForInstructorDto from(EvaluationAttempt attempt) {
        if (attempt == null) return null;
        EvaluationAttemptForInstructorDto dto = new EvaluationAttemptForInstructorDto();
        dto.setId(attempt.getId());
        dto.setEvaluationId(attempt.getEvaluation() != null ? attempt.getEvaluation().getId() : null);
        dto.setEvaluationTitle(attempt.getEvaluation() != null ? attempt.getEvaluation().getTitle() : null);
        dto.setUserId(attempt.getUser() != null ? attempt.getUser().getId() : null);
        dto.setLearnerName(attempt.getUser() != null ? (attempt.getUser().getFullName() != null ? attempt.getUser().getFullName() : attempt.getUser().getEmail()) : null);
        dto.setLearnerEmail(attempt.getUser() != null ? attempt.getUser().getEmail() : null);
        dto.setStatus(attempt.getStatus() != null ? attempt.getStatus().name() : null);
        dto.setScore(attempt.getScore());
        dto.setSubmittedFileUrl(attempt.getSubmittedFileUrl());
        dto.setSubmittedText(attempt.getSubmittedText());
        dto.setInstructorFeedback(attempt.getInstructorFeedback());
        dto.setCorrectedAt(attempt.getCorrectedAt());
        dto.setCreatedAt(attempt.getCreatedAt() != null ? attempt.getCreatedAt() : null);
        return dto;
    }
}
