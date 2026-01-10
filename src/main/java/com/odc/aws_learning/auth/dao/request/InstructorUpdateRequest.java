package com.odc.aws_learning.auth.dao.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class InstructorUpdateRequest {
    @Schema(description = "Biographie de l'instructeur", example = "Expert en Cloud Computing avec 10 ans d'expérience.")
    private String biography;

    @Schema(description = "Spécialisation de l'instructeur", example = "AWS, Azure")
    private String specialization;

    // Champs User pour permettre la mise à jour des informations utilisateur
    @Schema(description = "Nom complet de l'utilisateur")
    private String fullName;

    @Schema(description = "Email de l'utilisateur")
    private String email;

    @Schema(description = "Téléphone de l'utilisateur")
    private String phone;

    @Schema(description = "Avatar de l'utilisateur")
    private String avatar;

    @Schema(description = "Statut d'activation de l'utilisateur")
    private Boolean activate;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }
}
