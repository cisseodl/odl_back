package com.odc.aws_learning.app.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    // Contexte utilisateur (student)
    private Long coursesJoined;          // Nombre de cours rejoints
    private Long certificatesObtained;   // Nombre de certificats obtenus
    private Double averageScore;         // Moyenne générale (en %)
    private Long totalQuizAttempts;      // Nombre total de quiz tentés

    // Contexte administrateur (global)
    private Long totalUsers;             // Nombre total d'utilisateurs inscrits
    private Long totalCourses;           // Nombre total de cours créés
    private Long totalQuizAttemptsGlobal;// Nombre total de tentatives de quiz
    private Long totalCertificatesGlobal;// Nombre total de certificats délivrés

    // Rôle / type de dashboard ("STUDENT" ou "ADMIN")
    private String mode;
}
