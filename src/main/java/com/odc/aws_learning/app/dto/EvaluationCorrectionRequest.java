package com.odc.aws_learning.app.dto;

import lombok.Data;

@Data
public class EvaluationCorrectionRequest {
    private Long attemptId;
    private Double score; // Note entre 0 et 100
    private String feedback; // Commentaires de l'instructeur (optionnel)
}
