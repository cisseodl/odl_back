package com.odc.aws_learning.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

/**
 * DTO pour la création et la mise à jour d'une définition de lab.
 */
@Data
public class LabDefinitionRequest {
    
    @NotBlank(message = "Le titre est requis")
    private String title;
    
    private String description;
    
    /**
     * Fichiers uploadés (JSON array de chemins/URLs)
     * Format: ["/path/to/file1.pdf", "/path/to/file2.zip", ...]
     */
    private String uploadedFiles;
    
    /**
     * Liens ressources externes (JSON array d'URLs)
     * Format: ["https://example.com/resource1", "https://example.com/resource2", ...]
     */
    private String resourceLinks;
    
    @NotBlank(message = "Les instructions sont requises")
    private String instructions;
    
    @NotNull(message = "La durée estimée est requise")
    @Min(value = 1, message = "La durée estimée doit être supérieure à 0")
    private Integer estimatedDurationMinutes;
    
    private Boolean activate = true;
}

