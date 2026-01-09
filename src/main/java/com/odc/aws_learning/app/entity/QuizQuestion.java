package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Removed
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.util.ArrayList; // Added for default list initialization
import java.util.List;
import java.util.Objects; // Added for equals/hashCode

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity
@Table(name = "quiz_question")
// @Data // Removed
public class QuizQuestion extends BaseEntity {
    
    @Lob
    private String contenu;
    
    @Enumerated(EnumType.STRING)
    private QuestionType type; // QCM ou TEXTE
    
    private Integer points; // Points attribués pour cette question
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    // @JsonIgnoreProperties(value = {"questions"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (corresponds to Quiz.questions @JsonManagedReference)
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnoreProperties(value = {"question"}, allowSetters = true) // Replaced by @JsonManagedReference
    @JsonManagedReference // Added (corresponds to QuizReponse.question @JsonBackReference)
    private List<QuizReponse> reponses = new ArrayList<>(); // Initialize to avoid NullPointerException
    
    public QuizQuestion() {
        super();
    }

    public QuizQuestion(String contenu, QuestionType type, Integer points, Quiz quiz, List<QuizReponse> reponses) {
        this.contenu = contenu;
        this.type = type;
        this.points = points;
        this.quiz = quiz;
        this.reponses = reponses;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<QuizReponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<QuizReponse> reponses) {
        this.reponses = reponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuizQuestion that = (QuizQuestion) o;
        return Objects.equals(contenu, that.contenu) &&
               type == that.type &&
               Objects.equals(points, that.points) &&
               Objects.equals(quiz, that.quiz) &&
               Objects.equals(reponses, that.reponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contenu, type, points, quiz, reponses);
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
               "contenu='" + contenu + '\'' +
               ", type=" + type +
               ", points=" + points +
               ", quiz=" + (quiz != null ? quiz.getId() : "null") +
               ", reponses=" + (reponses != null ? reponses.size() : "null") +
               ", id=" + id +
               '}';
    }
    
    public enum QuestionType {
        QCM,    // Question à choix multiples
        TEXTE   // Question à réponse libre
    }
}
