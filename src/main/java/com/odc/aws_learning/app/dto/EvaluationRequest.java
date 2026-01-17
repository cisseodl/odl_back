package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.entity.Evaluations.EvaluationType;
import lombok.Data;

@Data
public class EvaluationRequest {
    private String title;
    private String description;
    private Long courseId;
    private EvaluationType type; // QUIZ ou TP
    private String tpInstructions; // Pour les TPs
    private String tpFileUrl; // Pour les TPs
}
