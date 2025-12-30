package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.wrapper.DashboardStatsDTO;
import com.odc.aws_learning.auth.entities.Role;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    /**
     * Statistiques pour un étudiant (USER / LEARNER)
     */
    public DashboardStatsDTO getStudentStats(User user) {
        Long userId = user.getId();

        long coursesJoined = userQuizAttemptRepository.countDistinctCoursesByUserId(userId);
        long certificatesObtained = userQuizAttemptRepository.countCertificatesByUserId(userId);
        long totalQuizAttempts = userQuizAttemptRepository.countByUserId(userId);
        Double avgScore = userQuizAttemptRepository.averageScorePercentageByUserId(userId);

        if (avgScore == null) {
            avgScore = 0.0;
        }

        return DashboardStatsDTO.builder()
                .coursesJoined(coursesJoined)
                .certificatesObtained(certificatesObtained)
                .totalQuizAttempts(totalQuizAttempts)
                .averageScore(avgScore)
                .mode("STUDENT")
                .build();
    }

    /**
     * Statistiques globales pour un administrateur (ADMIN / SUPERADMIN)
     */
    public DashboardStatsDTO getAdminStats() {
        long totalUsers = userRepository.count();
        long totalCourses = coursesRepository.count();
        long totalQuizAttemptsGlobal = userQuizAttemptRepository.count();
        long totalCertificatesGlobal = userQuizAttemptRepository.countAllCertificates();

        return DashboardStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalCourses(totalCourses)
                .totalQuizAttemptsGlobal(totalQuizAttemptsGlobal)
                .totalCertificatesGlobal(totalCertificatesGlobal)
                .mode("ADMIN")
                .build();
    }

    /**
     * Routeur en fonction du rôle de l'utilisateur
     */
    public DashboardStatsDTO getDashboardForUser(User user) {
        Role role = user.getRole();
        if (role == Role.ADMIN || role == Role.SUPERADMIN) {
            return getAdminStats();
        }
        return getStudentStats(user);
    }
}
