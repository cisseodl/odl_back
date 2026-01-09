package com.odc.aws_learning.app.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardStatsDTO {

    private AdminStats adminStats;
    private InstructorStats instructorStats;
    private StudentStats studentStats;
    private String mode; // ADMIN, INSTRUCTOR, STUDENT

    @Data
    @Builder
    public static class AdminStats {
        private long totalUsers;
        private long newUsersLast7Days;
        private long newUsersLast30Days;
        private Map<String, Long> usersByRole;
        private long totalCourses;
        private long newCoursesLast7Days;
        private long newCoursesLast30Days;
        private long publishedCourses;
        private long draftCourses;
        private long totalEnrollments;
        private Map<String, Long> top5CoursesByEnrollment;
        private long lessonsCompleted;
        private long totalQuizzes;
        private long pendingModeration;
    }

    @Data
    @Builder
    public static class InstructorStats {
        private long totalCourses;
        private long publishedCourses;
        private long draftCourses;
        private long totalEnrollments;
        private long newEnrollmentsLast7Days;
        private long newEnrollmentsLast30Days;
        private long activeLearners;
        private double averageCompletionRate;
        private double averageQuizScore;
        private long newComments;
        private Double averageRating; // Note moyenne des cours de l'instructeur
        private long totalStudents; // Total des apprenants uniques
        private double totalRevenue; // Revenus totaux (0.0 si gratuit)
    }

    @Data
    @Builder
    public static class StudentStats {
        private long coursesJoined;
        private long certificatesObtained;
        private double averageScore;
        private long totalQuizAttempts;
    }
}