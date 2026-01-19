package com.odc.aws_learning.app.dto;

public class OdcFormationRequest {
    private String titre;
    private String description;
    private String lien;

    public OdcFormationRequest() {
    }

    public OdcFormationRequest(String titre, String description, String lien) {
        this.titre = titre;
        this.description = description;
        this.lien = lien;
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
}
