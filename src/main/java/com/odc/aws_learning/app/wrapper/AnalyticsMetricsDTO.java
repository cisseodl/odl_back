package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsMetricsDTO {
    // Note moyenne globale
    private Double averageRating;
    private Long totalReviews;
    
    // Taux d'engagement (pourcentage d'utilisateurs actifs)
    private Double engagementRate;
    private Long activeUsers;
    private Long inactiveUsers;
    private Long totalUsers;
    
    // Temps d'apprentissage (approximatif basé sur UserProgress)
    private Double averageSessionTimeMinutes; // Approximation
    private Long activeSessions; // Basé sur les utilisateurs avec activité récente
    private Double interactionRate; // Pourcentage d'utilisateurs qui ont complété au moins une leçon
}
