package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.UserQuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizAttemptRepository extends JpaRepository<UserQuizAttempt, Long> {

    List<UserQuizAttempt> findByUserId(Long userId);

    List<UserQuizAttempt> findByQuizId(Long quizId);

    List<UserQuizAttempt> findByUserIdAndQuizId(Long userId, Long quizId);

    Optional<UserQuizAttempt> findFirstByUserIdAndQuizIdOrderByCreatedAtDesc(Long userId, Long quizId);

    Optional<UserQuizAttempt> findFirstByUserIdAndQuizIdOrderByScoreDesc(Long userId, Long quizId);

    long countByUserId(Long userId);

    /**
     * Nombre de cours rejoints par un utilisateur (basé sur les quiz tentés)
     */
    @Query("SELECT COUNT(DISTINCT a.quiz.course.id) FROM UserQuizAttempt a WHERE a.user.id = :userId")
    long countDistinctCoursesByUserId(@Param("userId") Long userId);

    /**
     * Nombre de certificats obtenus par un utilisateur
     * (nombre de quiz pour lesquels au moins une tentative atteint le score minimum).
     */
    @Query("SELECT COUNT(DISTINCT a.quiz.id) " +
           "FROM UserQuizAttempt a " +
           "WHERE a.user.id = :userId " +
           "AND a.score >= a.quiz.scoreMinimum")
    long countCertificatesByUserId(@Param("userId") Long userId);

    /**
     * Moyenne des scores (en pourcentage) pour un utilisateur.
     */
    @Query("SELECT AVG( (a.score / a.scoreTotal) * 100.0 ) " +
           "FROM UserQuizAttempt a " +
           "WHERE a.user.id = :userId AND a.scoreTotal > 0")
    Double averageScorePercentageByUserId(@Param("userId") Long userId);

    /**
     * Nombre total de certificats délivrés (toutes tentatives dont le score atteint le minimum).
     */
    @Query("SELECT COUNT(a) FROM UserQuizAttempt a WHERE a.score >= a.quiz.scoreMinimum")
    long countAllCertificates();

    @Query("SELECT AVG((a.score / a.scoreTotal) * 100.0) FROM UserQuizAttempt a WHERE a.quiz.course.instructor.id = :instructorId AND a.scoreTotal > 0")
    Double findAverageScoreByInstructorId(@Param("instructorId") Long instructorId);

    Optional<UserQuizAttempt> findFirstByUser_IdAndQuiz_Course_IdOrderByScoreDesc(Long userId, Long courseId);
}
