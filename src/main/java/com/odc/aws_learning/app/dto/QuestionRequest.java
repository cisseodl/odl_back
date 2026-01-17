package com.odc.aws_learning.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionRequest {
    private String title;
    private String description;
    private String type; // SINGLE_CHOICE, MULTIPLE_CHOICE, etc.
    private Integer points; // Points pour cette question
    private List<ResponseRequest> reponses; // Réponses possibles
}
