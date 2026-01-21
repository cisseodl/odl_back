package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.auth.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprenantWithUserDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Boolean activate;
    
    // Apprenant specific fields
    private String username; // Combine nom et prenom
    private String numero;
    private String profession;
    private String niveauEtude;
    private String filiere;
    private String attentes;
    private Boolean conditionsAccepted; // Acceptation des conditions (anciennement satisfaction)
    
    // Méthode de compatibilité pour l'ancien nom (deprecated)
    @Deprecated
    public Boolean getSatisfaction() {
        return conditionsAccepted;
    }
    
    @Deprecated
    public void setSatisfaction(Boolean satisfaction) {
        this.conditionsAccepted = satisfaction;
    }
    private Long cohorteId;
    private String cohorteNom;
    
    // User data
    private Long userId;
    private String fullName;
    private String userEmail;
    private String phone;
    private String avatar;
    private Boolean userActivate;
    
    public static ApprenantWithUserDto fromApprenant(Apprenant apprenant) {
        if (apprenant == null) {
            return null;
        }
        
        User user = apprenant.getUser();
        
        // Construire le username à partir de nom et prenom
        String username = null;
        if (apprenant.getNom() != null || apprenant.getPrenom() != null) {
            String nom = apprenant.getNom() != null ? apprenant.getNom() : "";
            String prenom = apprenant.getPrenom() != null ? apprenant.getPrenom() : "";
            username = (prenom + " " + nom).trim();
            if (username.isEmpty()) {
                username = nom.isEmpty() ? prenom : nom;
            }
        }
        
        ApprenantWithUserDto.ApprenantWithUserDtoBuilder builder = ApprenantWithUserDto.builder()
                .id(apprenant.getId())
                .createdAt(apprenant.getCreatedAt())
                .lastModifiedAt(apprenant.getLastModifiedAt())
                .activate(apprenant.isActivate())
                .username(username)
                .numero(apprenant.getNumero())
                .profession(apprenant.getProfession())
                .niveauEtude(apprenant.getNiveauEtude())
                .filiere(apprenant.getFiliere())
                .attentes(apprenant.getAttentes())
                .conditionsAccepted(apprenant.getConditionsAccepted());
        
        if (apprenant.getCohorte() != null) {
            builder.cohorteId(apprenant.getCohorte().getId())
                   .cohorteNom(apprenant.getCohorte().getNom());
        }
        
        if (user != null) {
            builder.userId(user.getId())
                   .fullName(user.getFullName())
                   .userEmail(user.getEmail())
                   .phone(user.getPhone())
                   .avatar(user.getAvatar())
                   .userActivate(user.getActivate());
        }
        
        return builder.build();
    }
}
