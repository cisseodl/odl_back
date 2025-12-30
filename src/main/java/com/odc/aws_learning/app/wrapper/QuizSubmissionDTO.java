package com.odc.aws_learning.app.wrapper;

import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionDTO {
    private Long quizId;
    private List<AnswerDTO> answers;
    
    @Data
    public static class AnswerDTO {
        private Long questionId;
        private List<Long> reponseIds; // Pour QCM, peut être multiple
        private String texteReponse; // Pour TEXTE, réponse libre
    }
}
