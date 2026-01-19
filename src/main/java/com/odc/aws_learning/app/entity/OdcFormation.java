package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "odc_formations")
public class OdcFormation extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String lien;

    // Relation ManyToOne avec User (Admin qui a créé la formation)
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    public OdcFormation() {
    }

    public OdcFormation(String titre, String description, String lien, User admin) {
        this.titre = titre;
        this.description = description;
        this.lien = lien;
        this.admin = admin;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
