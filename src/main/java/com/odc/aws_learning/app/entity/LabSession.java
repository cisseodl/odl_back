package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Removed
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity
@Table(name = "lab_session")
// @Data // Removed
public class LabSession extends BaseEntity {
    
    /**
     * Utilisateur (étudiant) qui a lancé cette session
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    // @JsonIgnoreProperties(value = {"labSessions"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (assuming User has a List<LabSession>)
    private User user;
    
    /**
     * Définition du lab associé
     */
    @ManyToOne
    @JoinColumn(name = "lab_definition_id", nullable = false)
    // @JsonIgnoreProperties(value = {"sessions"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (assuming LabDefinition has a List<LabSession>)
    private LabDefinition labDefinition;
    
    /**
     * Statut de la session (STARTING, RUNNING, STOPPED, SUBMITTED)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabSessionStatus status;
    
    /**
     * URL du conteneur/sandbox accessible par l'étudiant
     * (ex: "http://ec2-k8s-sandbox.aws.com/lab/abc-123-def")
     */
    @Column(name = "container_url")
    private String containerUrl;
    
    /**
     * Date et heure de démarrage de la session
     */
    @Column(name = "start_time", columnDefinition = "DATETIME")
    private LocalDateTime startTime;
    
    /**
     * Date et heure de fin de la session
     */
    @Column(name = "end_time", columnDefinition = "DATETIME")
    private LocalDateTime endTime;
    
    /**
     * Note/grade attribuée à la session (peut être "A", "B", "C", "PASS", "FAIL", etc.)
     */
    private String grade;
    
    /**
     * URL du rapport soumis par l'étudiant (optionnel)
     */
    @Column(name = "report_url")
    private String reportUrl;
    
    // Removed @PrePersist method. startTime should be handled by service.

    public LabSession() {
        super();
    }

    public LabSession(User user, LabDefinition labDefinition, LabSessionStatus status, String containerUrl, LocalDateTime startTime, LocalDateTime endTime, String grade, String reportUrl) {
        this.user = user;
        this.labDefinition = labDefinition;
        this.status = status;
        this.containerUrl = containerUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.grade = grade;
        this.reportUrl = reportUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LabDefinition getLabDefinition() {
        return labDefinition;
    }

    public void setLabDefinition(LabDefinition labDefinition) {
        this.labDefinition = labDefinition;
    }

    public LabSessionStatus getStatus() {
        return status;
    }

    public void setStatus(LabSessionStatus status) {
        this.status = status;
    }

    public String getContainerUrl() {
        return containerUrl;
    }

    public void setContainerUrl(String containerUrl) {
        this.containerUrl = containerUrl;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabSession that = (LabSession) o;
        return status == that.status &&
               Objects.equals(user, that.user) &&
               Objects.equals(labDefinition, that.labDefinition) &&
               Objects.equals(containerUrl, that.containerUrl) &&
               Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime) &&
               Objects.equals(grade, that.grade) &&
               Objects.equals(reportUrl, that.reportUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, labDefinition, status, containerUrl, startTime, endTime, grade, reportUrl);
    }

    @Override
    public String toString() {
        return "LabSession{" +
               "user=" + (user != null ? user.getId() : "null") +
               ", labDefinition=" + (labDefinition != null ? labDefinition.getId() : "null") +
               ", status=" + status +
               ", containerUrl='" + containerUrl + '\'' +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               ", grade='" + grade + '\'' +
               ", reportUrl='" + reportUrl + '\'' +
               ", id=" + id +
               '}';
    }
}
