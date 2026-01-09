package com.odc.aws_learning.auth.dao.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class InstructorUpdateRequest {
    @Schema(description = "Biographie de l'instructeur", example = "Expert en Cloud Computing avec 10 ans d'expérience.")
    private String biography;

    @Schema(description = "Spécialisation de l'instructeur", example = "AWS, Azure")
    private String specialization;

    // Si on veut mettre à jour les infos de l'utilisateur lié, on peut ajouter un UserUpdateDto ici.
    // Pour l'instant, on se concentre sur les champs spécifiques de l'instructeur.

    public InstructorUpdateRequest() {
    }

    public InstructorUpdateRequest(String biography, String specialization) {
        this.biography = biography;
        this.specialization = specialization;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
