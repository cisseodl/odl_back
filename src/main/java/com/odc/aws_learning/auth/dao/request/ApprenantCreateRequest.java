package com.odc.aws_learning.auth.dao.request;

import lombok.Data;

@Data
public class ApprenantCreateRequest {

    private Boolean activate;

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
    
    // Pour permettre aux admins de créer un apprenant pour un autre utilisateur
    private Long userId;
    private String userEmail; // Alternative à userId
}