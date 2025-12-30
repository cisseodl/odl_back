package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une session de lab pour un étudiant.
 * Conforme au schéma : session_id, user_id, statut, résultat.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lab_session")
@Data
public class LabSession extends BaseEntity {
    
    /**
     * Utilisateur (étudiant) qui a lancé cette session
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"labSessions"}, allowSetters = true)
    private User user;
    
    /**
     * Définition du lab associé
     */
    @ManyToOne
    @JoinColumn(name = "lab_definition_id", nullable = false)
    @JsonIgnoreProperties(value = {"sessions"}, allowSetters = true)
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
    
    /**
     * Initialise automatiquement startTime lors de la création
     */
    @PrePersist
    protected void onCreate() {
        if (startTime == null && status == LabSessionStatus.STARTING) {
            startTime = LocalDateTime.now();
        }
    }
}
