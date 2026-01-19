package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.entity.OdcFormation;

import java.time.LocalDateTime;

public class OdcFormationDto {
    private Long id;
    private String titre;
    private String description;
    private String lien;
    private Long adminId;
    private String adminName;
    private String adminEmail;
    private boolean activate;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public OdcFormationDto() {
    }

    public OdcFormationDto(OdcFormation formation) {
        this.id = formation.getId();
        this.titre = formation.getTitre();
        this.description = formation.getDescription();
        this.lien = formation.getLien();
        this.activate = formation.isActivate();
        this.createdBy = formation.getCreatedBy();
        this.createdAt = formation.getCreatedAt();
        this.lastModifiedAt = formation.getLastModifiedAt();
        
        if (formation.getAdmin() != null) {
            this.adminId = formation.getAdmin().getId();
            this.adminName = formation.getAdmin().getFullName();
            this.adminEmail = formation.getAdmin().getEmail();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
