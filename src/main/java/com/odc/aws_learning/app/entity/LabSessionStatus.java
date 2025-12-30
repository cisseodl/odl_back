package com.odc.aws_learning.app.entity;

/**
 * Enum représentant les différents statuts d'une session de lab.
 */
public enum LabSessionStatus {
    STARTING,   // La session est en cours de démarrage
    RUNNING,    // La session est active et l'étudiant peut travailler
    STOPPED,    // La session a été arrêtée
    SUBMITTED   // L'étudiant a soumis son travail
}
