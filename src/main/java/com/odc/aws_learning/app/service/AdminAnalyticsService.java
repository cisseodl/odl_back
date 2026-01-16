package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.CourseStatus;
import com.odc.aws_learning.app.entity.ActivityLog;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.app.repository.ActivityLogRepository;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.QuizRepository;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.wrapper.AnalyticsMetricsDTO;
import com.odc.aws_learning.app.wrapper.LearnerProgressResponseDTO;
import com.odc.aws_learning.app.wrapper.CoursePerformanceDataPoint;
import com.odc.aws_learning.app.wrapper.DashboardStatsDTO;
import com.odc.aws_learning.app.wrapper.LearningTimeMetricsDTO;
import com.odc.aws_learning.app.wrapper.ModerationSummaryData;
import com.odc.aws_learning.app.wrapper.OverallComparisonStats;
import com.odc.aws_learning.app.wrapper.UserActivitySummaryDTO;
import com.odc.aws_learning.app.wrapper.UserGrowthDataPoint;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.AdminRepository;
import com.odc.aws_learning.auth.repository.InstructorRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final UserProgressRepository userProgressRepository;
    private final QuizRepository quizRepository;
    private final AdminRepository adminRepository;
    private final InstructorRepository instructorRepository;
    private final ApprenantRepository apprenantRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final ReviewRepository reviewRepository; // Added for course performance
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public DashboardStatsDTO.AdminStats getAdminOverview() {
        LocalDateTime now = LocalDateTime.now();

        // User stats
        long totalUsers = userRepository.count();
        long newUsers7Days = apprenantRepository.countByCreatedAtAfter(now.minusDays(7));
        long newUsers30Days = apprenantRepository.countByCreatedAtAfter(now.minusDays(30));

        Map<String, Long> usersByRole = new HashMap<>();
        usersByRole.put("ADMIN", adminRepository.count());
        usersByRole.put("INSTRUCTOR", instructorRepository.count());
        usersByRole.put("LEARNER", apprenantRepository.count());

        // Course stats
        long totalCourses = coursesRepository.count();
        long newCourses7Days = coursesRepository.countByCreatedAtAfter(now.minusDays(7));
        long newCourses30Days = coursesRepository.countByCreatedAtAfter(now.minusDays(30));
        long publishedCourses = coursesRepository.countByStatus(CourseStatus.PUBLIE);
        long draftCourses = coursesRepository.countByStatus(CourseStatus.BROUILLON);

        // Enrollment stats
        long totalEnrollments = detailsCourseRepo.count();
        List<Object[]> topCoursesData = detailsCourseRepo.findTop5CoursesByEnrollments();
        Map<String, Long> top5Courses = topCoursesData.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((BigInteger) row[1]).longValue()
                ));


        // Activity stats
        long lessonsCompleted = userProgressRepository.count();
        long totalQuizzes = quizRepository.count();
        long pendingModeration = coursesRepository.countByStatus(CourseStatus.BROUILLON);


        return DashboardStatsDTO.AdminStats.builder()
                .totalUsers(totalUsers)
                .newUsersLast7Days(newUsers7Days)
                .newUsersLast30Days(newUsers30Days)
                .usersByRole(usersByRole)
                .totalCourses(totalCourses)
                .newCoursesLast7Days(newCourses7Days)
                .newCoursesLast30Days(newCourses30Days)
                .publishedCourses(publishedCourses)
                .draftCourses(draftCourses)
                .totalEnrollments(totalEnrollments)
                .top5CoursesByEnrollment(top5Courses)
                .lessonsCompleted(lessonsCompleted)
                .totalQuizzes(totalQuizzes)
                .pendingModeration(pendingModeration)
                .build();
    }

    public Page<UserActivitySummaryDTO> getUserActivitySummary(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);

        List<UserActivitySummaryDTO> summaries = users.getContent().stream().map(user -> {
            long coursesEnrolled = detailsCourseRepo.countByLearnerId(user.getId());
            long coursesCompleted = detailsCourseRepo.countByLearner_IdAndCompleted(user.getId(), true);
            Double avgScore = userQuizAttemptRepository.averageScorePercentageByUserId(user.getId());
            Optional<ActivityLog> lastLog = activityLogRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId());

            return UserActivitySummaryDTO.builder()
                    .userId(user.getId())
                    .userName(user.getFullName())
                    .userEmail(user.getEmail())
                    .coursesEnrolled(coursesEnrolled)
                    .coursesCompleted(coursesCompleted)
                    .averageQuizScore(avgScore)
                    .lastSeen(lastLog.map(ActivityLog::getCreatedAt).orElse(null))
                    .build();
        }).collect(Collectors.toList());

        return new PageImpl<>(summaries, pageable, users.getTotalElements());
    }

    public ModerationSummaryData getModerationSummary() {
        long pendingCourses = coursesRepository.countByStatus(CourseStatus.BROUILLON);
        // Placeholders for other pending items as per schema constraints
        long pendingInstructorProfiles = 0; // Requires status field on InstructorProfile
        long pendingReviews = 0; // Requires status field on Review
        long flaggedContent = 0; // Requires entity for flagged content

        long totalPending = pendingCourses + pendingInstructorProfiles + pendingReviews + flaggedContent;

        return ModerationSummaryData.builder()
                .pendingCourses(pendingCourses)
                .pendingInstructorProfiles(pendingInstructorProfiles)
                .pendingReviews(pendingReviews)
                .flaggedContent(flaggedContent)
                .totalPending(totalPending)
                .build();
    }

    public OverallComparisonStats getComparisonStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentPeriodStart = now.minusDays(30);
        LocalDateTime previousPeriodStart = now.minusDays(60);

        // Registrations
        long registrationsCurrentPeriod = apprenantRepository.countByCreatedAtAfter(currentPeriodStart);
        long registrationsPreviousPeriod = apprenantRepository.countByCreatedAtBetween(previousPeriodStart, currentPeriodStart);

        // Courses Created
        long coursesCreatedCurrentPeriod = coursesRepository.countByCreatedAtAfter(currentPeriodStart);
        long coursesCreatedPreviousPeriod = coursesRepository.countByCreatedAtBetween(previousPeriodStart, currentPeriodStart);

        // Completion Rate (simplified: count completed enrollments / total enrollments in period)
        long totalEnrollmentsCurrentPeriod = detailsCourseRepo.countByCreatedAtAfter(currentPeriodStart);
        long completedEnrollmentsCurrentPeriod = detailsCourseRepo.countByCompletedTrueAndCreatedAtAfter(currentPeriodStart);
        double completionRateCurrentPeriod = (totalEnrollmentsCurrentPeriod > 0) ? ((double) completedEnrollmentsCurrentPeriod / totalEnrollmentsCurrentPeriod) * 100.0 : 0.0;

        long totalEnrollmentsPreviousPeriod = detailsCourseRepo.countByCreatedAtBetween(previousPeriodStart, currentPeriodStart);
        long completedEnrollmentsPreviousPeriod = detailsCourseRepo.countByCompletedTrueAndCreatedAtBetween(previousPeriodStart, currentPeriodStart);
        double completionRatePreviousPeriod = (totalEnrollmentsPreviousPeriod > 0) ? ((double) completedEnrollmentsPreviousPeriod / totalEnrollmentsPreviousPeriod) * 100.0 : 0.0;

        // Active Users (simplified: users with any activity log entry in the period)
        long activeUsersCurrentPeriod = activityLogRepository.countDistinctUserByCreatedAtAfter(currentPeriodStart);
        long activeUsersPreviousPeriod = activityLogRepository.countDistinctUserByCreatedAtBetween(previousPeriodStart, currentPeriodStart);
        
        // Total users (tous les utilisateurs existants)
        long totalUsers = userRepository.count();
        
        // Inactive Users = Total Users - Active Users
        // Pour la période actuelle : utilisateurs sans activité dans la période actuelle
        long inactiveUsersCurrentPeriod = Math.max(0, totalUsers - activeUsersCurrentPeriod);
        // Pour la période précédente : utilisateurs sans activité dans la période précédente
        long inactiveUsersPreviousPeriod = Math.max(0, totalUsers - activeUsersPreviousPeriod);

        return OverallComparisonStats.builder()
                .registrationsCurrentPeriod(registrationsCurrentPeriod)
                .registrationsPreviousPeriod(registrationsPreviousPeriod)
                .coursesCreatedCurrentPeriod(coursesCreatedCurrentPeriod)
                .coursesCreatedPreviousPeriod(coursesCreatedPreviousPeriod)
                .completionRateCurrentPeriod(completionRateCurrentPeriod)
                .completionRatePreviousPeriod(completionRatePreviousPeriod)
                .activeUsersCurrentPeriod(activeUsersCurrentPeriod)
                .activeUsersPreviousPeriod(activeUsersPreviousPeriod)
                .inactiveUsersCurrentPeriod(inactiveUsersCurrentPeriod)
                .inactiveUsersPreviousPeriod(inactiveUsersPreviousPeriod)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserGrowthDataPoint> getUserGrowthData(String timeframe) {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<UserGrowthDataPoint> dataPoints = new ArrayList<>();

            switch (timeframe) {
            case "7-days":
                for (int i = 6; i >= 0; i--) {
                    LocalDateTime startOfDay = now.minusDays(i).truncatedTo(ChronoUnit.DAYS);
                    LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
                    long newUsers = apprenantRepository.countByCreatedAtBetween(startOfDay, endOfDay);
                    long totalUsers = apprenantRepository.countByCreatedAtBefore(endOfDay);
                    dataPoints.add(UserGrowthDataPoint.builder()
                            .date(startOfDay.toLocalDate().format(DateTimeFormatter.ISO_DATE))
                            .newUsers(newUsers)
                            .totalUsers(totalUsers)
                            .build());
                }
                break;
            case "3-months":
                for (int i = 2; i >= 0; i--) { // Last 3 full months + current partial month
                    LocalDateTime startOfMonth;
                    LocalDateTime endOfMonth;

                    if (i == 0) { // Current month
                        startOfMonth = now.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = now; // Data up to now
                    } else { // Previous months
                        startOfMonth = now.minusMonths(i).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
                    }
                    long newUsers = apprenantRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
                    long totalUsers = apprenantRepository.countByCreatedAtBefore(endOfMonth);

                    dataPoints.add(UserGrowthDataPoint.builder()
                            .date(startOfMonth.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                            .newUsers(newUsers)
                            .totalUsers(totalUsers)
                            .build());
                }
                break;
            case "6-months":
                 for (int i = 5; i >= 0; i--) { // Last 6 full months + current partial month
                    LocalDateTime startOfMonth;
                    LocalDateTime endOfMonth;

                    if (i == 0) { // Current month
                        startOfMonth = now.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = now; // Data up to now
                    } else { // Previous months
                        startOfMonth = now.minusMonths(i).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
                    }
                    long newUsers = apprenantRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
                    long totalUsers = apprenantRepository.countByCreatedAtBefore(endOfMonth);

                    dataPoints.add(UserGrowthDataPoint.builder()
                            .date(startOfMonth.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                            .newUsers(newUsers)
                            .totalUsers(totalUsers)
                            .build());
                }
                break;
            case "1-year":
                for (int i = 11; i >= 0; i--) { // Last 12 full months + current partial month
                    LocalDateTime startOfMonth;
                    LocalDateTime endOfMonth;

                    if (i == 0) { // Current month
                        startOfMonth = now.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = now; // Data up to now
                    } else { // Previous months
                        startOfMonth = now.minusMonths(i).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                        endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
                    }
                    long newUsers = apprenantRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
                    long totalUsers = apprenantRepository.countByCreatedAtBefore(endOfMonth);

                    dataPoints.add(UserGrowthDataPoint.builder()
                            .date(startOfMonth.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                            .newUsers(newUsers)
                            .totalUsers(totalUsers)
                            .build());
                }
                break;
            case "all":
                // For 'all', we might just return a single data point or monthly/yearly aggregates from start
                // For simplicity, let's return a single data point for total users and total new users ever
                long totalNewUsers = apprenantRepository.count();
                long totalCumulativeUsers = apprenantRepository.count(); // Assuming all users are Apprenants for this context
                 dataPoints.add(UserGrowthDataPoint.builder()
                            .date("All Time")
                            .newUsers(totalNewUsers)
                            .totalUsers(totalCumulativeUsers)
                            .build());
                break;
            default:
                throw new IllegalArgumentException("Invalid timeframe: " + timeframe + ". Valid values: 7-days, 3-months, 6-months, 1-year, all");
            }
            return dataPoints;
        } catch (Exception e) {
            System.err.println("Error in getUserGrowthData with timeframe '" + timeframe + "': " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching user growth data: " + e.getMessage(), e);
        }
    }

    public List<CoursePerformanceDataPoint> getCoursePerformanceData(String timeFilter, String startDateStr, String endDateStr) {
        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        if ("custom".equals(timeFilter) && startDateStr != null && endDateStr != null) {
            start = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
            end = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);
        } else {
            switch (timeFilter) {
                case "7d":
                    start = end.minusDays(7).truncatedTo(ChronoUnit.DAYS);
                    break;
                case "30d":
                    start = end.minusDays(30).truncatedTo(ChronoUnit.DAYS);
                    break;
                case "90d":
                    start = end.minusDays(90).truncatedTo(ChronoUnit.DAYS);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid timeFilter: " + timeFilter);
            }
        }

        List<Courses> allCourses = coursesRepository.findAll(); // Or filter by active/published
        List<CoursePerformanceDataPoint> dataPoints = new ArrayList<>();

        for (Courses course : allCourses) {
            long enrollments = detailsCourseRepo.countByCourseIdAndCreatedAtBetween(course.getId(), start, end);
            long completedEnrollments = detailsCourseRepo.countByCourseIdAndCompletedTrueAndCreatedAtBetween(course.getId(), start, end);
            double completionRate = (enrollments > 0) ? ((double) completedEnrollments / enrollments) * 100.0 : 0.0;
            Double averageRating = reviewRepository.findAverageRatingByCourseIdAndCreatedAtBetween(course.getId(), start, end);

            dataPoints.add(CoursePerformanceDataPoint.builder()
                    .courseId(course.getId())
                    .courseTitle(course.getTitle())
                    .enrollments(enrollments)
                    .completionRate(completionRate)
                    .averageRating(averageRating != null ? averageRating : 0.0)
                    .period(start.toLocalDate().format(DateTimeFormatter.ISO_DATE) + " to " + end.toLocalDate().format(DateTimeFormatter.ISO_DATE)) // Simplified period representation
                    .build());
        }
        return dataPoints;
    }

    public Page<ActivityLog> getAuditLogs(Pageable pageable, Long userId, String action, String resource, String startDate, String endDate) {
        Specification<ActivityLog> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            if (action != null && !action.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("action")), "%" + action.toLowerCase() + "%"));
            }
            if (resource != null && !resource.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("resource")), "%" + resource.toLowerCase() + "%"));
            }
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE).atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDateTime));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return activityLogRepository.findAll(spec, pageable);
    }

    public AnalyticsMetricsDTO getAnalyticsMetrics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last30Days = now.minusDays(30);

        // Note moyenne globale
        Double averageRating = reviewRepository.findAll().stream()
                .filter(r -> r.getRating() != null)
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0);
        Long totalReviews = reviewRepository.count();

        // Utilisateurs actifs (ayant une activité dans les 30 derniers jours)
        long activeUsers = activityLogRepository.countDistinctUserByCreatedAtAfter(last30Days);
        long totalUsers = userRepository.count();
        long inactiveUsers = Math.max(0, totalUsers - activeUsers);
        double engagementRate = totalUsers > 0 ? ((double) activeUsers / totalUsers) * 100.0 : 0.0;

        // Sessions actives (utilisateurs avec activité dans les 7 derniers jours)
        long activeSessions = activityLogRepository.countDistinctUserByCreatedAtAfter(now.minusDays(7));

        // Temps moyen par session (approximation basée sur UserProgress - on peut utiliser 24 min comme approximation)
        // Pour une vraie implémentation, il faudrait une entité Session avec durée
        double averageSessionTimeMinutes = 24.0; // Approximation

        // Taux d'interaction (utilisateurs qui ont complété au moins une leçon)
        long usersWithProgress = userProgressRepository.findAll().stream()
                .map(up -> up.getUser().getId())
                .distinct()
                .count();
        double interactionRate = totalUsers > 0 ? ((double) usersWithProgress / totalUsers) * 100.0 : 0.0;

        return AnalyticsMetricsDTO.builder()
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .engagementRate(engagementRate)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .totalUsers(totalUsers)
                .averageSessionTimeMinutes(averageSessionTimeMinutes)
                .activeSessions(activeSessions)
                .interactionRate(interactionRate)
                .build();
    }

    public LearnerProgressResponseDTO getLearnerProgress(Long learnerId) {
        // learnerId peut être soit l'ID de l'apprenant, soit l'ID de l'utilisateur
        // On essaie d'abord de trouver un apprenant avec cet ID
        Optional<Apprenant> apprenantOptional = apprenantRepository.findById(learnerId);
        User learner;
        
        if (apprenantOptional.isPresent()) {
            // Si c'est un ID d'apprenant, récupérer l'utilisateur associé
            Apprenant apprenant = apprenantOptional.get();
            if (apprenant.getUser() == null) {
                throw new RuntimeException("Apprenant with id " + learnerId + " has no associated user.");
            }
            learner = apprenant.getUser();
        } else {
            // Sinon, assumer que c'est un ID d'utilisateur
            Optional<User> userOptional = userRepository.findById(learnerId);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("Learner not found with id: " + learnerId);
            }
            learner = userOptional.get();
        }
        
        Long userId = learner.getId();

        // Get all enrolled courses for this learner
        List<DetailsCourse> enrolledCourses = detailsCourseRepo.findByLearnerId(userId);
        int coursesEnrolled = enrolledCourses.size();
        int coursesCompleted = (int) enrolledCourses.stream()
                .filter(DetailsCourse::isCompleted)
                .count();

        // Calculate overall progress across all courses
        List<LearnerProgressResponseDTO.CourseProgressDTO> courseProgressList = new ArrayList<>();
        double totalProgress = 0.0;
        int coursesWithProgress = 0;

        for (DetailsCourse detailsCourse : enrolledCourses) {
            Courses course = detailsCourse.getCourse();
            List<Module> modules = moduleRepository.findByCourseId(course.getId());
            
            int totalChapters = 0;
            int chaptersCompleted = 0;
            
            for (Module module : modules) {
                List<Lesson> lessons = lessonRepository.findByModuleId(module.getId());
                totalChapters += lessons.size();
            }
            
            // Get all completed lessons for this course
            List<UserProgress> allProgressForCourse = userProgressRepository.findByUserIdAndLessonModuleCourseId(userId, course.getId());
            chaptersCompleted = (int) allProgressForCourse.stream()
                    .map(up -> up.getLesson().getId())
                    .distinct()
                    .count();
            
            double courseProgress = totalChapters > 0 ? ((double) chaptersCompleted / totalChapters) * 100.0 : 0.0;
            if (totalChapters > 0) {
                totalProgress += courseProgress;
                coursesWithProgress++;
            }
            
            // Format period (e.g., "2025-01")
            String period = detailsCourse.getCreatedAt() != null 
                    ? detailsCourse.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    : "";
            
            courseProgressList.add(LearnerProgressResponseDTO.CourseProgressDTO.builder()
                    .courseId(course.getId())
                    .courseTitle(course.getTitle())
                    .courseOverallProgress(courseProgress)
                    .chaptersCompleted(chaptersCompleted)
                    .totalChapters(totalChapters)
                    .period(period)
                    .build());
        }
        
        double overallProgress = coursesWithProgress > 0 ? totalProgress / coursesWithProgress : 0.0;

        return LearnerProgressResponseDTO.builder()
                .id(String.valueOf(userId))
                .name(learner.getFullName() != null ? learner.getFullName() : "Utilisateur sans nom")
                .email(learner.getEmail())
                .coursesEnrolled(coursesEnrolled)
                .coursesCompleted(coursesCompleted)
                .overallProgress(overallProgress)
                .courses(courseProgressList)
                .build();
    }

    /**
     * Calcule les métriques de temps d'apprentissage :
     * - Temps moyen par cours
     * - Sessions actives
     * - Temps moyen par apprenant
     */
    public LearningTimeMetricsDTO getLearningTimeMetrics() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime last24Hours = now.minusHours(24);

            // Sessions actives : utilisateurs avec activité dans les dernières 24h
            long activeSessions = activityLogRepository.countDistinctUserByCreatedAtAfter(last24Hours);

            // Calculer le temps moyen par cours
            // Utiliser une requête optimisée au lieu de charger toutes les données
            List<Courses> allCourses = coursesRepository.findAll();
            List<Double> averageTimesPerCourse = new ArrayList<>();

            for (Courses course : allCourses) {
                try {
                    // Utiliser la méthode findByUserIdAndLessonModuleCourseId qui est plus optimisée
                    // Récupérer les UserProgress pour ce cours de manière optimisée
                    List<UserProgress> allProgressForCourse = userProgressRepository.findAll().stream()
                            .filter(up -> {
                                try {
                                    return up.getLesson() != null 
                                            && up.getLesson().getModule() != null 
                                            && up.getLesson().getModule().getCourse() != null
                                            && up.getLesson().getModule().getCourse().getId().equals(course.getId());
                                } catch (Exception e) {
                                    // Ignorer les entités avec des relations non chargées
                                    return false;
                                }
                            })
                            .collect(Collectors.toList());

                    if (!allProgressForCourse.isEmpty()) {
                        // Calculer le temps total passé sur ce cours (somme des durées des leçons complétées)
                        double totalTimeMinutes = allProgressForCourse.stream()
                                .filter(up -> {
                                    try {
                                        return up.getLesson() != null && up.getLesson().getDuration() != null;
                                    } catch (Exception e) {
                                        return false;
                                    }
                                })
                                .mapToDouble(up -> {
                                    try {
                                        return up.getLesson().getDuration() != null ? up.getLesson().getDuration().doubleValue() : 0.0;
                                    } catch (Exception e) {
                                        return 0.0;
                                    }
                                })
                                .sum();

                        // Compter les apprenants distincts qui ont complété au moins une leçon dans ce cours
                        long learnersCount = allProgressForCourse.stream()
                                .map(up -> {
                                    try {
                                        return up.getUser() != null ? up.getUser().getId() : null;
                                    } catch (Exception e) {
                                        return null;
                                    }
                                })
                                .filter(id -> id != null)
                                .distinct()
                                .count();

                        if (learnersCount > 0) {
                            double averageTimeForCourse = totalTimeMinutes / learnersCount;
                            averageTimesPerCourse.add(averageTimeForCourse);
                        }
                    }
                } catch (Exception e) {
                    // Continuer avec le cours suivant en cas d'erreur
                    System.err.println("Erreur lors du calcul des métriques pour le cours " + course.getId() + ": " + e.getMessage());
                }
            }

            // Temps moyen par cours (moyenne de tous les temps moyens par cours)
            double averageTimePerCourseMinutes = averageTimesPerCourse.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            // Calculer le temps moyen par apprenant de manière optimisée
            List<Double> timePerLearner = new ArrayList<>();
            
            // Utiliser une approche plus efficace : récupérer uniquement les UserProgress nécessaires
            // Limiter à un nombre raisonnable pour éviter les problèmes de mémoire
            try {
                // Utiliser une pagination ou limiter le nombre de résultats
                List<UserProgress> allProgress;
                try {
                    allProgress = userProgressRepository.findAll();
                    // Limiter à 10000 résultats maximum pour éviter les problèmes de mémoire
                    if (allProgress.size() > 10000) {
                        allProgress = allProgress.subList(0, 10000);
                    }
                } catch (OutOfMemoryError | Exception e) {
                    System.err.println("Erreur lors du chargement des UserProgress, utilisation d'une approche alternative: " + e.getMessage());
                    // Retourner des valeurs par défaut si le chargement échoue
                    allProgress = new ArrayList<>();
                }
                
                Map<Long, Double> timeByLearner = new HashMap<>();
                
                for (UserProgress up : allProgress) {
                    try {
                        if (up != null && up.getUser() != null && up.getLesson() != null && up.getLesson().getDuration() != null) {
                            Long userId = up.getUser().getId();
                            if (userId != null) {
                                Double duration = up.getLesson().getDuration().doubleValue();
                                timeByLearner.put(userId, timeByLearner.getOrDefault(userId, 0.0) + duration);
                            }
                        }
                    } catch (Exception e) {
                        // Ignorer les entités avec des relations non chargées
                        continue;
                    }
                }
                
                timePerLearner.addAll(timeByLearner.values());
            } catch (Exception e) {
                System.err.println("Erreur lors du calcul du temps par apprenant: " + e.getMessage());
                e.printStackTrace();
            }

            // Temps moyen par apprenant
            double averageTimePerLearnerMinutes = timePerLearner.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            // Nombre de cours avec activité
            long coursesWithActivity = (long) averageTimesPerCourse.size();

            // Nombre d'apprenants avec activité
            long learnersWithActivity = (long) timePerLearner.size();

            return LearningTimeMetricsDTO.builder()
                    .averageTimePerCourseMinutes(averageTimePerCourseMinutes)
                    .activeSessions(activeSessions)
                    .averageTimePerLearnerMinutes(averageTimePerLearnerMinutes)
                    .coursesWithActivity(coursesWithActivity)
                    .learnersWithActivity(learnersWithActivity)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            // Retourner des valeurs par défaut en cas d'erreur
            return LearningTimeMetricsDTO.builder()
                    .averageTimePerCourseMinutes(0.0)
                    .activeSessions(0L)
                    .averageTimePerLearnerMinutes(0.0)
                    .coursesWithActivity(0L)
                    .learnersWithActivity(0L)
                    .build();
        }
    }
}
