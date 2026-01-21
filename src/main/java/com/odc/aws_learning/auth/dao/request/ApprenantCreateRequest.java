package com.odc.aws_learning.auth.dao.request;

import lombok.Data;

@Data
public class ApprenantCreateRequest {

    private Boolean activate;

    private String username; // Remplace nom et prenom
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
    
    // Pour permettre aux admins de créer un apprenant pour un autre utilisateur
    private Long userId;
    private String userEmail; // Alternative à userId
}