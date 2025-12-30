package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.entity.QuizQuestion;
import lombok.Data;

import java.util.List;

@Data
public class QuizDTO {
    private Long id;
    private String titre;
    private String description;
    private Long courseId;
    private Integer dureeMinutes;
    private Integer scoreMinimum;
    private List<QuestionDTO> questions;
    
    @Data
    public static class QuestionDTO {
        private Long id;
        private String contenu;
        private QuizQuestion.QuestionType type;
        private Integer points;
        private List<ReponseDTO> reponses;
    }
    
    @Data
    public static class ReponseDTO {
        private Long id;
        private String texte;
        private Boolean estCorrecte;
    }
}
