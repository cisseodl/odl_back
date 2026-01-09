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
@Table(name = "quiz")
// @Data // Removed
public class Quiz extends BaseEntity {
    
    private String titre;
    
    @Lob
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    // @JsonIgnoreProperties(value = {"quiz"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (assuming Courses has a List<Quiz>)
    private Courses course;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnoreProperties(value = {"quiz"}, allowSetters = true) // Replaced by @JsonManagedReference
    @JsonManagedReference // Added (corresponds to QuizQuestion.quiz @JsonBackReference)
    private List<QuizQuestion> questions = new ArrayList<>(); // Initialize to avoid NullPointerException
    
    private Integer dureeMinutes; // Durée du quiz en minutes
    private Integer scoreMinimum; // Score minimum pour réussir

    public Quiz() {
        super();
    }

    public Quiz(String titre, String description, Courses course, List<QuizQuestion> questions, Integer dureeMinutes, Integer scoreMinimum) {
        this.titre = titre;
        this.description = description;
        this.course = course;
        this.questions = questions;
        this.dureeMinutes = dureeMinutes;
        this.scoreMinimum = scoreMinimum;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Integer getScoreMinimum() {
        return scoreMinimum;
    }

    public void setScoreMinimum(Integer scoreMinimum) {
        this.scoreMinimum = scoreMinimum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(titre, quiz.titre) &&
               Objects.equals(description, quiz.description) &&
               Objects.equals(course, quiz.course) &&
               Objects.equals(questions, quiz.questions) &&
               Objects.equals(dureeMinutes, quiz.dureeMinutes) &&
               Objects.equals(scoreMinimum, quiz.scoreMinimum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), titre, description, course, questions, dureeMinutes, scoreMinimum);
    }

    @Override
    public String toString() {
        return "Quiz{" +
               "titre='" + titre + '\'' +
               ", description='" + description + '\'' +
               ", course=" + (course != null ? course.getId() : "null") +
               ", questions=" + (questions != null ? questions.size() : "null") +
               ", dureeMinutes=" + dureeMinutes +
               ", scoreMinimum=" + scoreMinimum +
               ", id=" + id +
               '}';
    }
}
