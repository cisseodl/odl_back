package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Entité représentant la définition d'un exercice pratique (Lab).
 * Contient toutes les informations nécessaires pour créer une session de lab.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lab_definition")
@Data
public class LabDefinition extends BaseEntity {
    
    /**
     * Titre du lab (ex: "Déploiement d'une application Node.js sur AWS")
     */
    @Column(nullable = false)
    private String title;
    
    /**
     * Description détaillée de l'exercice
     */
    @Lob
    private String description;
    
    /**
     * Nom de l'image Docker à utiliser pour ce lab
     * (ex: "odl/aws-nodejs-lab:latest")
     */
    @Column(name = "docker_image_name")
    private String dockerImageName;
    
    /**
     * Instructions détaillées pour l'étudiant (format Markdown ou HTML)
     */
    @Lob
    private String instructions;
    
    /**
     * Durée estimée du lab en minutes
     */
    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;
    
    /**
     * Liste des sessions créées pour ce lab
     */
    @OneToMany(mappedBy = "labDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabSession> sessions;
}
