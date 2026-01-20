package com.odc.aws_learning.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO pour la création et la mise à jour d'une Formation
 */
@Data
public class FormationRequest {
    
    @NotBlank(message = "Le titre de la formation est requis")
    private String title;
    
    private String description;
    
    private String imagePath;
    
    @NotNull(message = "L'ID de la catégorie est requis")
    private Long categorieId;
    
    private Boolean activate = true;
}

