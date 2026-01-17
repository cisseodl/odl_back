package com.odc.aws_learning.app.entity;

// import com.fasterxml.jackson.annotation.JsonIgnore; // No longer needed for createdBy
import com.odc.aws_learning.auth.entities.User; // No longer needed to directly link createdBy
import com.odc.aws_learning.auth.base.entity.BaseEntity; // Added
// import lombok.AllArgsConstructor; // Lombok removed
// import lombok.Data; // Lombok removed
// import lombok.NoArgsConstructor; // Lombok removed

import javax.persistence.*;

@Entity
// @Data // Lombok removed
// @AllArgsConstructor // Lombok removed
// @NoArgsConstructor // Lombok removed
public class Rubrique extends BaseEntity { // Extends BaseEntity

    // id is now inherited from BaseEntity

    @Column(nullable = false)
    private String rubrique;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String objectifs;

    @Column(name = "public_cible", columnDefinition = "TEXT")
    private String publicCible;

    @Column(name = "duree_format")
    private String dureeFormat;

    @Column(name = "lien_ressources")
    private String lienRessources;

    @Column(name = "formations_proposees", columnDefinition = "TEXT")
    private String formationsProposees;

    // createdBy is now inherited as String from BaseEntity, no longer a ManyToOne User
    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // @JsonIgnore
    // private User createdBy;

    public Rubrique() {
    }

    public Rubrique(String rubrique, String image, String description, String objectifs, String publicCible, String dureeFormat, String lienRessources) {
        this.rubrique = rubrique;
        this.image = image;
        this.description = description;
        this.objectifs = objectifs;
        this.publicCible = publicCible;
        this.dureeFormat = dureeFormat;
        this.lienRessources = lienRessources;
    }

    public String getRubrique() {
        return rubrique;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(String objectifs) {
        this.objectifs = objectifs;
    }

    public String getPublicCible() {
        return publicCible;
    }

    public void setPublicCible(String publicCible) {
        this.publicCible = publicCible;
    }

    public String getDureeFormat() {
        return dureeFormat;
    }

    public void setDureeFormat(String dureeFormat) {
        this.dureeFormat = dureeFormat;
    }

    public String getLienRessources() {
        return lienRessources;
    }

    public void setLienRessources(String lienRessources) {
        this.lienRessources = lienRessources;
    }

    public String getFormationsProposees() {
        return formationsProposees;
    }

    public void setFormationsProposees(String formationsProposees) {
        this.formationsProposees = formationsProposees;
    }
}
