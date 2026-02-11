package com.odc.aws_learning.app.service;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.app.repository.ActivityLogRepository;
import com.odc.aws_learning.app.entity.ActivityLog;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.app.entity.Courses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final ReviewRepository reviewRepository;
    private final ActivityLogRepository activityLogRepository;
    private final com.odc.aws_learning.app.repository.ApprenantRepository apprenantRepository;
    // Potentially inject other repositories like InstructorRepository

    /**
     * Retrieves user growth data over a specified time period.
     * @param timeFilter "day", "week", "month", "year"
     * @param startDate Optional start date
     * @param endDate Optional end date
     * @return CResponse containing user growth data.
     */
    public CResponse<?> getUserGrowthData(String timeFilter, Instant startDate, Instant endDate) {
        try {
            // Déterminer la période de temps
            Instant start = startDate;
            Instant end = endDate;
            
            if (start == null || end == null) {
                // Si pas de dates spécifiées, utiliser timeFilter ou période par défaut
                Instant now = Instant.now();
                if (timeFilter != null) {
                    switch (timeFilter.toLowerCase()) {
                        case "7-days":
                        case "7d":
                            start = now.minus(7, ChronoUnit.DAYS);
                            end = now;
                            break;
                        case "3-months":
                        case "90d":
                            start = now.minus(90, ChronoUnit.DAYS);
                            end = now;
                            break;
                        case "6-months":
                        case "180d":
                            start = now.minus(180, ChronoUnit.DAYS);
                            end = now;
                            break;
                        case "1-year":
                        case "365d":
                            start = now.minus(365, ChronoUnit.DAYS);
                            end = now;
                            break;
                        default:
                            start = now.minus(30, ChronoUnit.DAYS);
                            end = now;
                    }
                } else {
                    start = now.minus(30, ChronoUnit.DAYS);
                    end = now;
                }
            }
            
            // Convertir Instant en LocalDateTime pour la requête
            java.time.LocalDateTime startLocal = java.time.LocalDateTime.ofInstant(start, java.time.ZoneId.systemDefault());
            java.time.LocalDateTime endLocal = java.time.LocalDateTime.ofInstant(end, java.time.ZoneId.systemDefault());
            
            // Récupérer les apprenants créés dans la période
            List<com.odc.aws_learning.app.entity.Apprenant> apprenants = apprenantRepository.findAll()
                .stream()
                .filter(a -> {
                    if (a.getCreatedAt() == null) return false;
                    java.time.LocalDateTime createdAt = a.getCreatedAt();
                    return !createdAt.isBefore(startLocal) && !createdAt.isAfter(endLocal);
                })
                .collect(Collectors.toList());
            
            // Grouper par jour ou mois selon la période
            long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(startLocal, endLocal);
            boolean groupByDay = daysDiff <= 90; // Grouper par jour si <= 90 jours, sinon par mois
            
            Map<String, Long> groupedData = new HashMap<>();
            for (com.odc.aws_learning.app.entity.Apprenant apprenant : apprenants) {
                if (apprenant.getCreatedAt() == null) continue;
                java.time.LocalDateTime createdAt = apprenant.getCreatedAt();
                String key;
                if (groupByDay) {
                    key = createdAt.toLocalDate().toString(); // Format: YYYY-MM-DD
                } else {
                    // Format: YYYY-MM (sans le jour)
                    java.time.LocalDate firstDayOfMonth = createdAt.toLocalDate().withDayOfMonth(1);
                    key = firstDayOfMonth.getYear() + "-" + 
                          String.format("%02d", firstDayOfMonth.getMonthValue());
                }
                groupedData.put(key, groupedData.getOrDefault(key, 0L) + 1);
            }
            
            // Convertir en liste de points de données
            List<Map<String, Object>> dataPoints = groupedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> point = new HashMap<>();
                    point.put("date", entry.getKey());
                    point.put("newUsers", entry.getValue());
                    // Calculer le total cumulé jusqu'à cette date
                    try {
                        java.time.LocalDateTime dateToCheck;
                        if (groupByDay) {
                            dateToCheck = java.time.LocalDate.parse(entry.getKey()).atTime(23, 59, 59);
                        } else {
                            // Pour les mois, utiliser le dernier jour du mois
                            java.time.LocalDate firstDay = java.time.LocalDate.parse(entry.getKey() + "-01");
                            java.time.LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                            dateToCheck = lastDay.atTime(23, 59, 59);
                        }
                        long totalUpToDate = apprenantRepository.countByCreatedAtBefore(dateToCheck);
                        point.put("totalUsers", totalUpToDate);
                    } catch (Exception e) {
                        // En cas d'erreur, utiliser le nombre total d'apprenants
                        point.put("totalUsers", apprenantRepository.count());
                    }
                    return point;
                })
                .collect(Collectors.toList());
            
            return CResponse.success(dataPoints, "User growth data retrieved successfully.");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des données de croissance: " + e.getMessage());
        }
    }

    /**
     * Retrieves course performance data.
     * @param timeFilter "day", "week", "month", "year"
     * @param startDate Optional start date
     * @param endDate Optional end date
     * @return CResponse containing course performance data.
     */
    public CResponse<?> getCoursePerformanceData(String timeFilter, Instant startDate, Instant endDate) {
        // Placeholder. Real implementation would involve complex queries on Courses, Enrollment, UserQuizAttempt
        // and potentially Review entities.
        List<Courses> courses = coursesRepository.findAll();
        List<Map<String, Object>> coursePerformance = courses.stream().map(course -> {
            Map<String, Object> courseStats = new HashMap<>();
            courseStats.put("courseId", course.getId());
            courseStats.put("courseTitle", course.getTitle());
            courseStats.put("enrollments", (long) (Math.random() * 100)); // Dummy
            courseStats.put("completionRate", Math.round(Math.random() * 10000.0) / 100.0); // Dummy %
            courseStats.put("averageRating", Math.round(Math.random() * 500.0) / 100.0); // Dummy 1-5
            return courseStats;
        }).collect(Collectors.toList());
        return CResponse.success(coursePerformance, "Course performance data retrieved successfully.");
    }

    /**
     * Retrieves comparison statistics for different periods.
     * @param period "monthly", "quarterly", "yearly"
     * @param timeFilter Defines the current period (e.g., "currentMonth", "lastMonth")
     * @return CResponse containing comparison stats.
     */
    public CResponse<?> getComparisonStats(String period, String timeFilter) {
        // Placeholder. Needs complex aggregation logic.
        Map<String, Object> data = new HashMap<>();
        data.put("registrationsCurrentPeriod", 100L);
        data.put("registrationsPreviousPeriod", 80L);
        data.put("completionRateCurrentPeriod", 75.5);
        data.put("completionRatePreviousPeriod", 70.0);
        data.put("coursesCreatedCurrentPeriod", 10L);
        data.put("coursesCreatedPreviousPeriod", 8L);
        data.put("activeUsersCurrentPeriod", 200L);
        data.put("activeUsersPreviousPeriod", 180L);
        return CResponse.success(data, "Comparison stats retrieved successfully.");
    }

    /**
     * Retrieves recent activity data for an instructor.
     * This will likely require an AuditLog entity and repository.
     * @param instructorId The ID of the instructor.
     * @param limit The maximum number of activities to return.
     * @return CResponse containing recent activity.
     */
    public CResponse<?> getInstructorRecentActivity(Long instructorId, int limit) {
        try {
            // Récupérer les vraies activités de l'instructeur depuis ActivityLog
            Pageable pageable = PageRequest.of(0, limit);
            List<ActivityLog> activityLogs = activityLogRepository.findByUserIdOrderByCreatedAtDesc(instructorId, pageable);
            
            // Mapper les ActivityLog vers le format attendu par le frontend
            List<Map<String, Object>> activity = activityLogs.stream().map(log -> {
                Map<String, Object> activityMap = new HashMap<>();
                
                // Déterminer le type d'activité basé sur l'action
                String activityType = "UNKNOWN";
                if (log.getAction() != null) {
                    String action = log.getAction().toUpperCase();
                    if (action.contains("CREATE") || action.contains("CREATED")) {
                        if (log.getResource() != null && log.getResource().toLowerCase().contains("course")) {
                            activityType = "COURSE_CREATED";
                        } else {
                            activityType = "CREATED";
                        }
                    } else if (action.contains("UPDATE") || action.contains("UPDATED")) {
                        if (log.getResource() != null && log.getResource().toLowerCase().contains("course")) {
                            activityType = "COURSE_UPDATED";
                        } else {
                            activityType = "UPDATED";
                        }
                    } else if (action.contains("DELETE") || action.contains("DELETED")) {
                        activityType = "DELETED";
                    } else if (action.contains("APPROVE") || action.contains("APPROVED")) {
                        activityType = "APPROVED";
                    } else if (action.contains("REJECT") || action.contains("REJECTED")) {
                        activityType = "REJECTED";
                    }
                }
                
                activityMap.put("activityType", activityType);
                activityMap.put("timestamp", log.getCreatedAt() != null ? log.getCreatedAt().toString() : Instant.now().toString());
                
                // Extraire le titre du cours depuis resource ou details
                String courseTitle = null;
                if (log.getResource() != null) {
                    // Format possible: "Course: Titre" ou "course:123"
                    if (log.getResource().contains(":")) {
                        String[] parts = log.getResource().split(":", 2);
                        if (parts.length > 1) {
                            courseTitle = parts[1].trim();
                        }
                    } else {
                        courseTitle = log.getResource();
                    }
                }
                
                // Essayer d'extraire depuis details si JSON
                if (log.getDetails() != null && log.getDetails().contains("courseTitle")) {
                    try {
                        if (log.getDetails().startsWith("{")) {
                            int titleIndex = log.getDetails().indexOf("\"courseTitle\"");
                            if (titleIndex > 0) {
                                int start = log.getDetails().indexOf("\"", titleIndex + 13) + 1;
                                int end = log.getDetails().indexOf("\"", start);
                                if (start > 0 && end > start) {
                                    courseTitle = log.getDetails().substring(start, end);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Ignorer les erreurs de parsing
                    }
                }
                
                activityMap.put("courseTitle", courseTitle != null ? courseTitle : "N/A");
                activityMap.put("courseId", extractIdFromResource(log.getResource()));
                return activityMap;
            }).collect(Collectors.toList());
            
            return CResponse.success(activity, "Instructor recent activity retrieved successfully.");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des activités: " + e.getMessage());
        }
    }
    
    private Long extractIdFromResource(String resource) {
        if (resource == null) return null;
        try {
            // Essayer d'extraire un ID depuis le resource (format: "Course: Title" ou "course:123")
            String[] parts = resource.split(":");
            if (parts.length > 1) {
                String lastPart = parts[parts.length - 1].trim();
                // Si c'est un nombre, le retourner
                if (lastPart.matches("\\d+")) {
                    return Long.parseLong(lastPart);
                }
            }
        } catch (Exception e) {
            // Ignorer les erreurs de parsing
        }
        return null;
    }

    /**
     * Retrieves moderation summary (e.g., pending contents, courses, reviews, instructors).
     * @return CResponse containing moderation summary.
     */
    public CResponse<?> getModerationSummary() {
        // Placeholder. Will query various entities for pending moderation status.
        Map<String, Long> summary = new HashMap<>();
        summary.put("pendingContents", 5L);
        summary.put("pendingCourses", 2L);
        summary.put("pendingReviews", 10L);
        summary.put("pendingInstructors", 1L);
        return CResponse.success(summary, "Moderation summary retrieved successfully.");
    }

    /**
     * Retrieves course performance data for a specific instructor.
     * @param instructorId The ID of the instructor.
     * @return CResponse containing course performance data for the instructor.
     */
    public CResponse<?> getCoursePerformanceForInstructor(Long instructorId) {
        // Query courses by instructor and aggregate real performance data
        List<Courses> instructorCourses = coursesRepository.findByInstructor_Id(instructorId);
        List<Map<String, Object>> coursePerformance = instructorCourses.stream().map(course -> {
            Map<String, Object> courseStats = new HashMap<>();
            courseStats.put("courseId", course.getId());
            courseStats.put("courseTitle", course.getTitle());
            
            // Get real students count from DetailsCourse
            long studentsCount = detailsCourseRepo.countByCourseId(course.getId());
            courseStats.put("studentsCount", studentsCount);
            
            // Calculate completion rate: completed enrollments / total enrollments
            long totalEnrollments = studentsCount;
            long completedEnrollments = detailsCourseRepo.countByCourseIdAndCompleted(course.getId(), true);
            double completionRate = totalEnrollments > 0 
                ? (completedEnrollments * 100.0 / totalEnrollments) 
                : 0.0;
            courseStats.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
            
            // Get average rating from ReviewRepository
            Double avgRating = reviewRepository.findAverageRatingByCourse(course);
            courseStats.put("averageRating", avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : 0.0);
            
            return courseStats;
        }).collect(Collectors.toList());
        return CResponse.success(coursePerformance, "Course performance for instructor retrieved successfully.");
    }

    /**
     * Retrieves course performance data by month for an instructor.
     * Returns ratings grouped by course and month.
     * @param instructorId The ID of the instructor.
     * @return CResponse containing course performance data by month.
     */
    public CResponse<?> getCoursePerformanceByMonthForInstructor(Long instructorId) {
        try {
            // Récupérer les cours de l'instructeur
            List<Courses> instructorCourses = coursesRepository.findByInstructor_Id(instructorId);
            
            // Calculer les 12 derniers mois
            java.time.LocalDate now = java.time.LocalDate.now();
            List<Map<String, Object>> monthlyData = new java.util.ArrayList<>();
            
            // Pour chaque mois des 12 derniers mois
            for (int i = 11; i >= 0; i--) {
                java.time.LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
                java.time.LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
                java.time.LocalDateTime monthStartDateTime = monthStart.atStartOfDay();
                java.time.LocalDateTime monthEndDateTime = monthEnd.atTime(23, 59, 59);
                
                Map<String, Object> monthData = new HashMap<>();
                String monthLabel = monthStart.format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.FRENCH));
                monthData.put("month", monthLabel);
                monthData.put("monthKey", monthStart.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
                
                // Pour chaque cours, calculer la note moyenne du mois
                Map<String, Double> courseRatings = new HashMap<>();
                for (Courses course : instructorCourses) {
                    Double avgRating = reviewRepository.findAverageRatingByCourseIdAndCreatedAtBetween(
                        course.getId(), 
                        monthStartDateTime, 
                        monthEndDateTime
                    );
                    if (avgRating != null && avgRating > 0) {
                        courseRatings.put(course.getTitle(), Math.round(avgRating * 100.0) / 100.0);
                    }
                }
                
                // Ajouter les notes de chaque cours au mois
                for (Courses course : instructorCourses) {
                    monthData.put(course.getTitle(), courseRatings.getOrDefault(course.getTitle(), 0.0));
                }
                
                monthlyData.add(monthData);
            }
            
            return CResponse.success(monthlyData, "Course performance by month for instructor retrieved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des performances par mois: " + e.getMessage());
        }
    }
}
