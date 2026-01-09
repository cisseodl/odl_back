package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Removed
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.time.LocalDateTime; // Keep only if other LocalDateTime fields are used
import java.util.Objects; // Added for equals/hashCode

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity
@Table(name = "user_quiz_attempt")
// @Data // Removed
public class UserQuizAttempt extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    // @JsonIgnoreProperties(value = {"quizAttempts"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (assuming User has a List<UserQuizAttempt>)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    // @JsonIgnoreProperties(value = {"attempts"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (assuming Quiz has a List<UserQuizAttempt>)
    private Quiz quiz;
    
    private Double score; // Score obtenu (peut être un pourcentage ou un nombre de points)
    
    private Integer scoreTotal; // Score total possible
    
    // Removed redundant dateTentative field and @PrePersist - inherited from BaseEntity (as createdAt)

    public UserQuizAttempt() {
        super();
    }

    public UserQuizAttempt(User user, Quiz quiz, Double score, Integer scoreTotal) { // Removed dateTentative
        this.user = user;
        this.quiz = quiz;
        this.score = score;
        this.scoreTotal = scoreTotal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(Integer scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    // Removed getDateTentative() and setDateTentative()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserQuizAttempt that = (UserQuizAttempt) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(quiz, that.quiz) &&
               Objects.equals(score, that.score) &&
               Objects.equals(scoreTotal, that.scoreTotal);
               // Objects.equals(dateTentative, that.dateTentative); // Removed
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, quiz, score, scoreTotal); // Removed dateTentative
    }

    @Override
    public String toString() {
        return "UserQuizAttempt{" +
               "user=" + (user != null ? user.getId() : "null") +
               ", quiz=" + (quiz != null ? quiz.getId() : "null") +
               ", score=" + score +
               ", scoreTotal=" + scoreTotal +
               ", dateTentative=" + getCreatedAt() + // Use inherited method
               ", id=" + id +
               '}';
    }
}
