package com.odc.aws_learning.app.dto;

import lombok.Data;
import java.util.Map;

@Data
public class EvaluationSubmissionRequest {
    private Long evaluationId;
    private String submittedFileUrl; // Pour les TPs (optionnel)
    private Map<Long, Long> answers; // Pour les QUIZ: questionId -> responseId
    private Map<Long, String> textAnswers; // Pour les QUIZ avec réponses libres: questionId -> réponse texte
}
