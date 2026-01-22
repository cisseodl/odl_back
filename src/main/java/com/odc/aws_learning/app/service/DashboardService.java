package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.CourseStatus;
import com.odc.aws_learning.app.repository.*;
import com.odc.aws_learning.app.wrapper.DashboardStatsDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final ReviewRepository reviewRepository;
    private final CertificateRepository certificateRepository;
    private final QuizRepository quizRepository;
    private final InstructorProfileRepository instructorProfileRepository;


    public DashboardStatsDTO getStudentStats(User user) {
        Long userId = user.getId();

        long coursesJoined = detailsCourseRepo.countByLearnerId(userId);
        long certificatesObtained = certificateRepository.countByUser(user);
        Double avgScore = userQuizAttemptRepository.averageScorePercentageByUserId(userId);

        DashboardStatsDTO.StudentStats studentStats = DashboardStatsDTO.StudentStats.builder()
                .coursesJoined(coursesJoined)
                .certificatesObtained(certificatesObtained)
                .averageScore(avgScore != null ? avgScore : 0.0)
                .totalQuizAttempts(userQuizAttemptRepository.countByUserId(userId))
                .build();

        return DashboardStatsDTO.builder()
                .studentStats(studentStats)
                .mode("STUDENT")
                .build();
    }


    public DashboardStatsDTO getAdminStats() {
        // This is a placeholder. The full implementation will be in a dedicated AdminAnalyticsService.
        DashboardStatsDTO.AdminStats adminStats = DashboardStatsDTO.AdminStats.builder()
                .totalUsers(userRepository.count())
                .totalCourses(coursesRepository.count())
                .totalEnrollments(detailsCourseRepo.count())
                .totalQuizzes(quizRepository.count())
                .build();

        return DashboardStatsDTO.builder()
                .adminStats(adminStats)
                .mode("ADMIN")
                .build();
    }


    public DashboardStatsDTO getDashboardForUser(User user) {
        if (user.getAdmin() != null) {
            return getAdminStats();
        } else if (user.getInstructor() != null) {
            return getInstructorStats(user);
        } else { // Handles both Apprenant and generic USER roles
            return getStudentStats(user);
        }
    }


    public DashboardStatsDTO getInstructorStats(User instructorUser) {
        Long instructorId = instructorUser.getId();
        LocalDateTime now = LocalDateTime.now();

        // Course stats
        long totalCourses = coursesRepository.countByInstructorId(instructorId);
        long publishedCourses = coursesRepository.countByInstructorIdAndStatus(instructorId, CourseStatus.PUBLIE);
        long draftCourses = coursesRepository.countByInstructorIdAndStatus(instructorId, CourseStatus.BROUILLON);

        // Enrollment stats
        long totalEnrollments = detailsCourseRepo.countByCourse_Instructor_Id(instructorId);
        long newEnrollments7Days = detailsCourseRepo.countByCourse_Instructor_IdAndCreatedAtAfter(instructorId, now.minusDays(7));
        long newEnrollments30Days = detailsCourseRepo.countByCourse_Instructor_IdAndCreatedAtAfter(instructorId, now.minusDays(30));

        // Learner activity
        long activeLearners = detailsCourseRepo.countDistinctLearnerByCourse_Instructor_IdAndLastModifiedAtAfter(instructorId, now.minusDays(30));

        // Performance stats
        long completedEnrollments = detailsCourseRepo.countByCourse_Instructor_IdAndCompleted(instructorId, true);
        double averageCompletionRate = (totalEnrollments > 0) ? ((double) completedEnrollments / totalEnrollments) * 100.0 : 0.0;
        Double averageQuizScore = userQuizAttemptRepository.findAverageScoreByInstructorId(instructorId);
        
        // Total certificates for instructor's courses (nombre de certifiés par module)
        long totalCertificatesByModule = certificateRepository.countByCourse_Instructor_Id(instructorId);

        // Interaction stats
        long newComments = reviewRepository.countByCourse_Instructor_IdAndCreatedAtAfter(instructorId, now.minusDays(30));

        // Average rating for instructor's courses
        Double averageRating = reviewRepository.findAverageRatingByInstructorCourses(instructorId);
        
        // Total unique students (learners enrolled in instructor's courses)
        Long totalStudentsLong = detailsCourseRepo.countDistinctLearnersByInstructorCourses(instructorId);
        long totalStudents = totalStudentsLong != null ? totalStudentsLong : 0;
        
        // Total revenue (assuming courses are free for now, set to 0.0)
        // TODO: Implement revenue calculation if courses become paid
        double totalRevenue = 0.0;

        DashboardStatsDTO.InstructorStats instructorStats = DashboardStatsDTO.InstructorStats.builder()
                .totalCourses(totalCourses)
                .publishedCourses(publishedCourses)
                .draftCourses(draftCourses)
                .totalEnrollments(totalEnrollments)
                .newEnrollmentsLast7Days(newEnrollments7Days)
                .newEnrollmentsLast30Days(newEnrollments30Days)
                .activeLearners(activeLearners)
                .averageCompletionRate(averageCompletionRate)
                .averageQuizScore(averageQuizScore != null ? averageQuizScore : 0.0)
                .totalCertificatesByModule(totalCertificatesByModule)
                .newComments(newComments)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalStudents(totalStudents)
                .totalRevenue(totalRevenue)
                .build();

        return DashboardStatsDTO.builder()
                .instructorStats(instructorStats)
                .mode("INSTRUCTOR")
                .build();
    }

    /**
     * Récupère les statistiques publiques pour la page d'accueil
     * Ces statistiques sont accessibles sans authentification
     */
    public CResponse<?> getPublicStats() {
        try {
            // Compter le nombre total d'étudiants actifs (utilisateurs avec au moins une inscription active)
            long totalStudents = detailsCourseRepo.countDistinctLearners();
            System.out.println("Total étudiants actifs: " + totalStudents);
            
            // Compter le nombre total de cours publiés
            long totalCourses = coursesRepository.countByStatus(CourseStatus.PUBLIE);
            System.out.println("Total cours publiés: " + totalCourses);
            
            // Compter le nombre total d'instructeurs actifs
            long totalInstructors = instructorProfileRepository.count();
            System.out.println("Total instructeurs actifs: " + totalInstructors);
            
            // Trouver le cours avec le maximum d'inscriptions (cours le plus consulté)
            Long maxEnrollments = detailsCourseRepo.findMaxEnrollmentsByCourse();
            long mostViewedCourses = maxEnrollments != null ? maxEnrollments : 0;
            System.out.println("Cours le plus consulté (max inscriptions): " + mostViewedCourses);
            
            // Calculer le taux de satisfaction moyen (moyenne des ratings de tous les cours)
            // On récupère tous les cours publiés et on calcule la moyenne des ratings
            Double averageRating = reviewRepository.findOverallAverageRating();
            // Convertir de 0-5 à 0-100 pour le pourcentage
            double satisfactionRate = averageRating != null ? Math.round((averageRating / 5.0) * 100) : 98;
            System.out.println("Taux de satisfaction: " + satisfactionRate + "% (rating moyen: " + averageRating + ")");
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStudents", totalStudents);
            stats.put("totalCourses", totalCourses);
            stats.put("totalInstructors", totalInstructors);
            stats.put("mostViewedCourses", mostViewedCourses);
            stats.put("satisfactionRate", satisfactionRate);
            
            System.out.println("Statistiques publiques: " + stats);
            
            return CResponse.success(stats, "Statistiques publiques récupérées avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des statistiques publiques: " + e.getMessage());
        }
    }
}
