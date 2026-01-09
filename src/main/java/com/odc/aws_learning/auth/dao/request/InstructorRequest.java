package com.odc.aws_learning.auth.dao.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class InstructorRequest {
    @Schema(description = "Biographie de l'instructeur", example = "Expert en Cloud Computing avec 10 ans d'expérience.")
    private String biography;

    @Schema(description = "Spécialisation de l'instructeur", example = "AWS, Azure")
    private String specialization;

    @Schema(description = "ID de l'utilisateur à promouvoir (optionnel, si non fourni, utilise l'utilisateur connecté)", example = "123")
    private Long userId;

    @Schema(description = "Email de l'utilisateur à promouvoir (optionnel, utilisé si userId n'est pas fourni)", example = "user@example.com")
    private String userEmail;

    public InstructorRequest() {
    }

    public InstructorRequest(String biography, String specialization) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
