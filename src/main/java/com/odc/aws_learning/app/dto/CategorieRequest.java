package com.odc.aws_learning.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class CategorieRequest {

    private Long id; // Ajouté

    @Schema(description = "Titre de la catégorie", example = "Cloud Computing")
    private String title;

    @Schema(description = "Description de la catégorie", example = "Introduction au Cloud et à ses services.")
    private String description;

    // Pas d'autres champs de l'entité Categorie, pas de relations

    public CategorieRequest() {
    }

    public CategorieRequest(Long id, String title, String description) { // Mis à jour le constructeur
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Long getId() { // Ajouté le getter pour id
        return id;
    }

    public void setId(Long id) { // Ajouté le setter pour id
        this.id = id;
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
}
