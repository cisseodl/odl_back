package com.odc.aws_learning.app.wrapper;

// import lombok.Data; // Removed

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

// @Data // Removed
public class QuizSubmissionDTO {
    private Long quizId;
    private List<AnswerDTO> answers = new ArrayList<>(); // Initialize list
    
    public QuizSubmissionDTO() {
    }

    public QuizSubmissionDTO(Long quizId, List<AnswerDTO> answers) {
        this.quizId = quizId;
        this.answers = answers;
    }

    // Getters and Setters for QuizSubmissionDTO
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public List<AnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDTO> answers) { this.answers = answers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizSubmissionDTO that = (QuizSubmissionDTO) o;
        return Objects.equals(quizId, that.quizId) && Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, answers);
    }

    @Override
    public String toString() {
        return "QuizSubmissionDTO{" +
               "quizId=" + quizId +
               ", answers=" + (answers != null ? answers.size() : "null") +
               '}';
    }


    // @Data // Removed
    public static class AnswerDTO {
        private Long questionId;
        private List<Long> reponseIds = new ArrayList<>(); // Pour QCM, peut être multiple
        private String texteReponse; // Pour TEXTE, réponse libre

        public AnswerDTO() {
        }

        public AnswerDTO(Long questionId, List<Long> reponseIds, String texteReponse) {
            this.questionId = questionId;
            this.reponseIds = reponseIds;
            this.texteReponse = texteReponse;
        }

        // Getters and Setters for AnswerDTO
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public List<Long> getReponseIds() { return reponseIds; }
        public void setReponseIds(List<Long> reponseIds) { this.reponseIds = reponseIds; }
        public String getTexteReponse() { return texteReponse; }
        public void setTexteReponse(String texteReponse) { this.texteReponse = texteReponse; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnswerDTO answerDTO = (AnswerDTO) o;
            return Objects.equals(questionId, answerDTO.questionId) &&
                   Objects.equals(reponseIds, answerDTO.reponseIds) &&
                   Objects.equals(texteReponse, answerDTO.texteReponse);
        }

        @Override
        public int hashCode() {
            return Objects.hash(questionId, reponseIds, texteReponse);
        }

        @Override
        public String toString() {
            return "AnswerDTO{" +
                   "questionId=" + questionId +
                   ", reponseIds=" + (reponseIds != null ? reponseIds.size() : "null") +
                   ", texteReponse='" + texteReponse + '\'' +
                   '}';
        }
    }
}

