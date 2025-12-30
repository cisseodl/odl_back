package com.odc.aws_learning.app.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private Long attemptId;
    private Long quizId;
    private String quizTitre;
    private Double score;
    private Integer scoreTotal;
    private Double pourcentage;
    private Boolean reussi; // true si score >= scoreMinimum
    private LocalDateTime dateTentative;
    private List<QuestionResultDTO> detailsQuestions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResultDTO {
        private Long questionId;
        private String questionContenu;
        private Integer pointsObtenus;
        private Integer pointsTotal;
        private Boolean correcte;
        private List<Long> reponsesCorrectes;
        private List<Long> reponsesUtilisateur;
    }
}
