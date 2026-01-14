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
    private String nom;
    private String prenom;
    private String email;
    private String numero;
    private String profession;
    private String niveauEtude;
    private String filiere;
    private String attentes;
    private Boolean satisfaction;
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
        ApprenantWithUserDto.ApprenantWithUserDtoBuilder builder = ApprenantWithUserDto.builder()
                .id(apprenant.getId())
                .createdAt(apprenant.getCreatedAt())
                .lastModifiedAt(apprenant.getLastModifiedAt())
                .activate(apprenant.isActivate())
                .nom(apprenant.getNom())
                .prenom(apprenant.getPrenom())
                .email(apprenant.getEmail())
                .numero(apprenant.getNumero())
                .profession(apprenant.getProfession())
                .niveauEtude(apprenant.getNiveauEtude())
                .filiere(apprenant.getFiliere())
                .attentes(apprenant.getAttentes())
                .satisfaction(apprenant.getSatisfaction());
        
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
