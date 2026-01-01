package com.odc.aws_learning.app.wrapper;

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

    // NoArgsConstructor
    public DashboardStatsDTO() {
    }

    // AllArgsConstructor
    public DashboardStatsDTO(Long coursesJoined, Long certificatesObtained, Double averageScore, Long totalQuizAttempts, Long totalUsers, Long totalCourses, Long totalQuizAttemptsGlobal, Long totalCertificatesGlobal, String mode) {
        this.coursesJoined = coursesJoined;
        this.certificatesObtained = certificatesObtained;
        this.averageScore = averageScore;
        this.totalQuizAttempts = totalQuizAttempts;
        this.totalUsers = totalUsers;
        this.totalCourses = totalCourses;
        this.totalQuizAttemptsGlobal = totalQuizAttemptsGlobal;
        this.totalCertificatesGlobal = totalCertificatesGlobal;
        this.mode = mode;
    }

    // Getters
    public Long getCoursesJoined() {
        return coursesJoined;
    }

    public Long getCertificatesObtained() {
        return certificatesObtained;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public Long getTotalQuizAttempts() {
        return totalQuizAttempts;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public Long getTotalCourses() {
        return totalCourses;
    }

    public Long getTotalQuizAttemptsGlobal() {
        return totalQuizAttemptsGlobal;
    }

    public Long getTotalCertificatesGlobal() {
        return totalCertificatesGlobal;
    }

    public String getMode() {
        return mode;
    }

    // Setters
    public void setCoursesJoined(Long coursesJoined) {
        this.coursesJoined = coursesJoined;
    }

    public void setCertificatesObtained(Long certificatesObtained) {
        this.certificatesObtained = certificatesObtained;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public void setTotalQuizAttempts(Long totalQuizAttempts) {
        this.totalQuizAttempts = totalQuizAttempts;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setTotalCourses(Long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public void setTotalQuizAttemptsGlobal(Long totalQuizAttemptsGlobal) {
        this.totalQuizAttemptsGlobal = totalQuizAttemptsGlobal;
    }

    public void setTotalCertificatesGlobal(Long totalCertificatesGlobal) {
        this.totalCertificatesGlobal = totalCertificatesGlobal;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    // Builder class
    public static DashboardStatsDTOBuilder builder() {
        return new DashboardStatsDTOBuilder();
    }

    public static class DashboardStatsDTOBuilder {
        private Long coursesJoined;
        private Long certificatesObtained;
        private Double averageScore;
        private Long totalQuizAttempts;
        private Long totalUsers;
        private Long totalCourses;
        private Long totalQuizAttemptsGlobal;
        private Long totalCertificatesGlobal;
        private String mode;

        DashboardStatsDTOBuilder() {
        }

        public DashboardStatsDTOBuilder coursesJoined(Long coursesJoined) {
            this.coursesJoined = coursesJoined;
            return this;
        }
        public DashboardStatsDTOBuilder totalStudentsInInstructorCourses(long totalStudentsInInstructorCourses) {
            this.coursesJoined = totalStudentsInInstructorCourses;
            return this;
        }

        public DashboardStatsDTOBuilder certificatesObtained(Long certificatesObtained) {
            this.certificatesObtained = certificatesObtained;
            return this;
        }

        public DashboardStatsDTOBuilder averageScore(Double averageScore) {
            this.averageScore = averageScore;
            return this;
        }
        public DashboardStatsDTOBuilder averageCourseRating(Double averageCourseRating) {
            this.averageScore = averageCourseRating;
            return this;
        }

        public DashboardStatsDTOBuilder totalQuizAttempts(Long totalQuizAttempts) {
            this.totalQuizAttempts = totalQuizAttempts;
            return this;
        }

        public DashboardStatsDTOBuilder totalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }

        public DashboardStatsDTOBuilder totalCourses(Long totalCourses) {
            this.totalCourses = totalCourses;
            return this;
        }
        public DashboardStatsDTOBuilder coursesCreated(long coursesCreated) {
            this.totalCourses = coursesCreated;
            return this;
        }

        public DashboardStatsDTOBuilder totalQuizAttemptsGlobal(Long totalQuizAttemptsGlobal) {
            this.totalQuizAttemptsGlobal = totalQuizAttemptsGlobal;
            return this;
        }

        public DashboardStatsDTOBuilder totalCertificatesGlobal(Long totalCertificatesGlobal) {
            this.totalCertificatesGlobal = totalCertificatesGlobal;
            return this;
        }

        public DashboardStatsDTOBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public DashboardStatsDTO build() {
            return new DashboardStatsDTO(coursesJoined, certificatesObtained, averageScore, totalQuizAttempts, totalUsers, totalCourses, totalQuizAttemptsGlobal, totalCertificatesGlobal, mode);
        }

        public String toString() {
            return "DashboardStatsDTO.DashboardStatsDTOBuilder(coursesJoined=" + this.coursesJoined + ", certificatesObtained=" + this.certificatesObtained + ", averageScore=" + this.averageScore + ", totalQuizAttempts=" + this.totalQuizAttempts + ", totalUsers=" + this.totalUsers + ", totalCourses=" + this.totalCourses + ", totalQuizAttemptsGlobal=" + this.totalQuizAttemptsGlobal + ", totalCertificatesGlobal=" + this.totalCertificatesGlobal + ", mode=" + this.mode + ")";
        }
    }
}