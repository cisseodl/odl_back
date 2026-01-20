package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "course_satisfaction")
public class CourseSatisfaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'apprenant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_attempt_id", nullable = false)
    private EvaluationAttempt evaluationAttempt; // La tentative d'examen associée

    @Column(nullable = false, columnDefinition = "TEXT")
    private String satisfaction; // L'impression de l'apprenant sur le cours

    @Column(nullable = true)
    private Integer rating; // Note de satisfaction (optionnelle, 1-5)

    public CourseSatisfaction() {
    }

    public CourseSatisfaction(Courses course, User user, EvaluationAttempt evaluationAttempt, String satisfaction) {
        this.course = course;
        this.user = user;
        this.evaluationAttempt = evaluationAttempt;
        this.satisfaction = satisfaction;
    }

    // Getters
    public Courses getCourse() {
        return course;
    }

    public User getUser() {
        return user;
    }

    public EvaluationAttempt getEvaluationAttempt() {
        return evaluationAttempt;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public Integer getRating() {
        return rating;
    }

    // Setters
    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvaluationAttempt(EvaluationAttempt evaluationAttempt) {
        this.evaluationAttempt = evaluationAttempt;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
