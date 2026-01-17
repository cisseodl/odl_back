package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "evaluation_attempts")
public class EvaluationAttempt extends BaseEntity {
    
    public enum AttemptStatus {
        PENDING,    // En attente de correction (pour les TPs)
        CORRECTED,  // Corrigé par l'instructeur
        PASSED,     // Réussi (score >= 70%)
        FAILED      // Échoué (score < 70%)
    }
    
    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    @JsonBackReference
    private Evaluations evaluation;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user; // Apprenant qui fait l'évaluation
    
    @Column(nullable = true)
    private Double score; // Score entre 0 et 100 (null si pas encore corrigé)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus status = AttemptStatus.PENDING;
    
    @Column(nullable = true, length = 1000)
    private String submittedFileUrl; // URL du fichier soumis (pour les TPs)
    
    @ManyToOne
    @JoinColumn(name = "corrected_by", nullable = true)
    @JsonBackReference
    private User correctedBy; // Instructeur qui corrige (pour les TPs)
    
    @Column(nullable = true)
    private java.time.Instant correctedAt;
    
    @Column(nullable = true, length = 2000)
    private String instructorFeedback; // Commentaires de l'instructeur
    
    public EvaluationAttempt() {
        super();
    }
    
    public EvaluationAttempt(Evaluations evaluation, User user) {
        this.evaluation = evaluation;
        this.user = user;
        this.status = AttemptStatus.PENDING;
    }
    
    // Getters
    public Evaluations getEvaluation() {
        return evaluation;
    }
    
    public User getUser() {
        return user;
    }
    
    public Double getScore() {
        return score;
    }
    
    public AttemptStatus getStatus() {
        return status;
    }
    
    public String getSubmittedFileUrl() {
        return submittedFileUrl;
    }
    
    public User getCorrectedBy() {
        return correctedBy;
    }
    
    public java.time.Instant getCorrectedAt() {
        return correctedAt;
    }
    
    public String getInstructorFeedback() {
        return instructorFeedback;
    }
    
    // Setters
    public void setEvaluation(Evaluations evaluation) {
        this.evaluation = evaluation;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setScore(Double score) {
        this.score = score;
        // Mettre à jour le statut automatiquement selon le score
        if (score != null) {
            if (score >= 70.0) {
                this.status = AttemptStatus.PASSED;
            } else {
                this.status = AttemptStatus.FAILED;
            }
        }
    }
    
    public void setStatus(AttemptStatus status) {
        this.status = status;
    }
    
    public void setSubmittedFileUrl(String submittedFileUrl) {
        this.submittedFileUrl = submittedFileUrl;
    }
    
    public void setCorrectedBy(User correctedBy) {
        this.correctedBy = correctedBy;
    }
    
    public void setCorrectedAt(java.time.Instant correctedAt) {
        this.correctedAt = correctedAt;
    }
    
    public void setInstructorFeedback(String instructorFeedback) {
        this.instructorFeedback = instructorFeedback;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EvaluationAttempt that = (EvaluationAttempt) o;
        return Objects.equals(evaluation, that.evaluation) &&
               Objects.equals(user, that.user) &&
               Objects.equals(score, that.score) &&
               status == that.status &&
               Objects.equals(submittedFileUrl, that.submittedFileUrl) &&
               Objects.equals(correctedBy, that.correctedBy) &&
               Objects.equals(correctedAt, that.correctedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), evaluation, user, score, status, submittedFileUrl, correctedBy, correctedAt);
    }
    
    @Override
    public String toString() {
        return "EvaluationAttempt{" +
               "evaluation=" + (evaluation != null ? evaluation.getId() : "null") +
               ", user=" + (user != null ? user.getId() : "null") +
               ", score=" + score +
               ", status=" + status +
               ", id=" + id +
               '}';
    }
}
