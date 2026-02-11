package com.odc.aws_learning.app.constante;

/**
 * Mode d'obtention du certificat pour un cours.
 * - BY_EXAM : l'apprenant passe l'évaluation créée par l'instructeur ; 70 % minimum pour être certifié.
 * - BY_LABS : l'instructeur valide les labs réalisés et attribue le certificat (email de félicitations envoyé).
 */
public enum CertificationMode {
    BY_EXAM,
    BY_LABS
}
