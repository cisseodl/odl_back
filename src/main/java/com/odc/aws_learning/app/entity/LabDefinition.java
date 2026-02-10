package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Added for Lesson
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.util.ArrayList; // Added for default list initialization
import java.util.List;
import java.util.Objects; // Added for equals/hashCode

/**
 * Entité représentant la définition d'un exercice pratique (Lab).
 * Contient toutes les informations nécessaires pour créer une session de lab.
 */
// @EqualsAndHashCode(callSuper = true) // Removed
@Entity
@Table(name = "lab_definition")
// @Data // Removed
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
     * Fichiers uploadés pour ce lab (JSON array de chemins/URLs)
     * Format: ["/path/to/file1.pdf", "/path/to/file2.zip", ...]
     */
    @Lob
    @Column(name = "uploaded_files")
    private String uploadedFiles;
    
    /**
     * Liens ressources externes pour ce lab (JSON array d'URLs)
     * Format: ["https://example.com/resource1", "https://example.com/resource2", ...]
     */
    @Lob
    @Column(name = "resource_links")
    private String resourceLinks;
    
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
     * Durée maximale du lab en minutes (le lab sera automatiquement arrêté après cette durée)
     */
    @Column(name = "max_duration_minutes")
    private Integer maxDurationMinutes;
    
    /**
     * Leçon associée à ce lab
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = true)
    @JsonIgnoreProperties({"labDefinitions"}) // Permet la sérialisation de la leçon sans boucle infinie
    private Lesson lesson;
    
    /**
     * Liste des sessions créées pour ce lab
     */
    @OneToMany(mappedBy = "labDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added (corresponds to LabSession.labDefinition @JsonBackReference)
    private List<LabSession> sessions = new ArrayList<>(); // Initialize to avoid NullPointerException

    public LabDefinition() {
        super();
    }

    public LabDefinition(String title, String description, String uploadedFiles, String resourceLinks, String instructions, Integer estimatedDurationMinutes, Integer maxDurationMinutes, Lesson lesson, List<LabSession> sessions) {
        this.title = title;
        this.description = description;
        this.uploadedFiles = uploadedFiles;
        this.resourceLinks = resourceLinks;
        this.instructions = instructions;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.maxDurationMinutes = maxDurationMinutes;
        this.lesson = lesson;
        this.sessions = sessions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(String uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public String getResourceLinks() {
        return resourceLinks;
    }

    public void setResourceLinks(String resourceLinks) {
        this.resourceLinks = resourceLinks;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public Integer getMaxDurationMinutes() {
        return maxDurationMinutes;
    }

    public void setMaxDurationMinutes(Integer maxDurationMinutes) {
        this.maxDurationMinutes = maxDurationMinutes;
    }

    public List<LabSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<LabSession> sessions) {
        this.sessions = sessions;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabDefinition that = (LabDefinition) o;
        return Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(uploadedFiles, that.uploadedFiles) &&
               Objects.equals(resourceLinks, that.resourceLinks) &&
               Objects.equals(instructions, that.instructions) &&
               Objects.equals(estimatedDurationMinutes, that.estimatedDurationMinutes) &&
               Objects.equals(maxDurationMinutes, that.maxDurationMinutes) &&
               Objects.equals(sessions, that.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, uploadedFiles, resourceLinks, instructions, estimatedDurationMinutes, maxDurationMinutes, sessions);
    }

    @Override
    public String toString() {
        return "LabDefinition{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", uploadedFiles='" + uploadedFiles + '\'' +
               ", resourceLinks='" + resourceLinks + '\'' +
               ", instructions='" + instructions + '\'' +
               ", estimatedDurationMinutes=" + estimatedDurationMinutes +
               ", maxDurationMinutes=" + maxDurationMinutes +
               ", sessions=" + (sessions != null ? sessions.size() : "null") +
               ", id=" + id +
               '}';
    }
}
