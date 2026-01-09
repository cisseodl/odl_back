package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.entity.QuizQuestion;
// import lombok.Data; // Removed

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

// @Data // Removed
public class QuizDTO {
    private Long id;
    private String title; // Changed from titre to title
    private String description;
    private Long courseId;
    private Integer durationMinutes; // Changed from dureeMinutes to durationMinutes
    private Integer scoreMinimum;
    private List<QuestionDTO> questions = new ArrayList<>(); // Initialize list

    public QuizDTO() {
    }

    public QuizDTO(Long id, String title, String description, Long courseId, Integer durationMinutes, Integer scoreMinimum, List<QuestionDTO> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.durationMinutes = durationMinutes;
        this.scoreMinimum = scoreMinimum;
        this.questions = questions;
    }

    // Getters and Setters for QuizDTO
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Integer getScoreMinimum() { return scoreMinimum; }
    public void setScoreMinimum(Integer scoreMinimum) { this.scoreMinimum = scoreMinimum; }
    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> questions) { this.questions = questions; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizDTO quizDTO = (QuizDTO) o;
        return Objects.equals(id, quizDTO.id) && Objects.equals(title, quizDTO.title) &&
               Objects.equals(description, quizDTO.description) && Objects.equals(courseId, quizDTO.courseId) &&
               Objects.equals(durationMinutes, quizDTO.durationMinutes) && Objects.equals(scoreMinimum, quizDTO.scoreMinimum) &&
               Objects.equals(questions, quizDTO.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, courseId, durationMinutes, scoreMinimum, questions);
    }

    @Override
    public String toString() {
        return "QuizDTO{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", courseId=" + courseId +
               ", durationMinutes=" + durationMinutes +
               ", scoreMinimum=" + scoreMinimum +
               ", questions=" + (questions != null ? questions.size() : "null") +
               '}';
    }


    // @Data // Removed
    public static class QuestionDTO {
        private Long id;
        private String content; // Changed from contenu to content
        private QuizQuestion.QuestionType type;
        private Integer points;
        private List<ReponseDTO> reponses = new ArrayList<>(); // Initialize list

        public QuestionDTO() {
        }

        public QuestionDTO(Long id, String content, QuizQuestion.QuestionType type, Integer points, List<ReponseDTO> reponses) {
            this.id = id;
            this.content = content;
            this.type = type;
            this.points = points;
            this.reponses = reponses;
        }

        // Getters and Setters for QuestionDTO
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public QuizQuestion.QuestionType getType() { return type; }
        public void setType(QuizQuestion.QuestionType type) { this.type = type; }
        public Integer getPoints() { return points; }
        public void setPoints(Integer points) { this.points = points; }
        public List<ReponseDTO> getReponses() { return reponses; }
        public void setReponses(List<ReponseDTO> reponses) { this.reponses = reponses; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QuestionDTO that = (QuestionDTO) o;
            return Objects.equals(id, that.id) && Objects.equals(content, that.content) &&
                   type == that.type && Objects.equals(points, that.points) &&
                   Objects.equals(reponses, that.reponses);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, content, type, points, reponses);
        }

        @Override
        public String toString() {
            return "QuestionDTO{" +
                   "id=" + id +
                   ", content='" + content + '\'' +
                   ", type=" + type +
                   ", points=" + points +
                   ", reponses=" + (reponses != null ? reponses.size() : "null") +
                   '}';
        }
    }
    
    // @Data // Removed
    public static class ReponseDTO {
        private Long id;
        private String text; // Changed from texte to text
        private Boolean isCorrect; // Changed from estCorrecte to isCorrect

        public ReponseDTO() {
        }

        public ReponseDTO(Long id, String text, Boolean isCorrect) {
            this.id = id;
            this.text = text;
            this.isCorrect = isCorrect;
        }

        // Getters and Setters for ReponseDTO
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public Boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReponseDTO that = (ReponseDTO) o;
            return Objects.equals(id, that.id) && Objects.equals(text, that.text) &&
                   Objects.equals(isCorrect, that.isCorrect);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, text, isCorrect);
        }

        @Override
        public String toString() {
            return "ReponseDTO{" +
                   "id=" + id +
                   ", text='" + text + '\'' +
                   ", isCorrect=" + isCorrect +
                   '}';
        }
    }
}

