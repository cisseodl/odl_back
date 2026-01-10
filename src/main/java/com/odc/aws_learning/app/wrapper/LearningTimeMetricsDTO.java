package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningTimeMetricsDTO {
    // Temps moyen passé par cours (en minutes)
    private Double averageTimePerCourseMinutes;
    
    // Nombre de sessions actives (utilisateurs avec activité dans les dernières 24h)
    private Long activeSessions;
    
    // Temps moyen passé par apprenant (en minutes)
    private Double averageTimePerLearnerMinutes;
    
    // Nombre total de cours avec activité
    private Long coursesWithActivity;
    
    // Nombre total d'apprenants avec activité
    private Long learnersWithActivity;
}
