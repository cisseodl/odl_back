package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Evaluations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationsRepository extends JpaRepository<Evaluations, Long> {
    @Query("SELECT e FROM Evaluations e WHERE e.course.id = :courseId")
    List<Evaluations> findByCourseId(@Param("courseId") Long courseId);

    /**
     * Charge une évaluation par id avec questions (un seul JOIN FETCH pour éviter MultipleBagFetchException).
     * Les réponses sont initialisées dans le service par accès .getReponses().size().
     */
    @Query("SELECT DISTINCT e FROM Evaluations e LEFT JOIN FETCH e.questions WHERE e.id = :id")
    java.util.Optional<Evaluations> findByIdWithQuestions(@Param("id") Long id);

    /**
     * Examen de fin de cours : évaluation de type QUIZ sans leçon associée (certification).
     * À ne pas confondre avec les TD (type TP, associés à une leçon).
     */
    @Query("SELECT e FROM Evaluations e WHERE e.course.id = :courseId AND e.type = 'QUIZ' AND e.lesson IS NULL")
    List<Evaluations> findCourseExamsByCourseId(@Param("courseId") Long courseId);

    /**
     * Tous les QUIZ du cours (avec ou sans leçon) — pour fallback si aucun "examen sans leçon".
     */
    @Query("SELECT e FROM Evaluations e WHERE e.course.id = :courseId AND e.type = 'QUIZ'")
    List<Evaluations> findQuizByCourseId(@Param("courseId") Long courseId);
    
    /**
     * Récupère toutes les évaluations avec leurs leçons, modules et cours chargés
     */
    @Query("SELECT DISTINCT e FROM Evaluations e " +
           "LEFT JOIN FETCH e.lesson lesson " +
           "LEFT JOIN FETCH lesson.module module " +
           "LEFT JOIN FETCH module.course course " +
           "LEFT JOIN FETCH e.course course2 " +
           "LEFT JOIN FETCH e.instructor")
    List<Evaluations> findAllWithLessonModuleAndCourse();

    /**
     * Évaluations de niveau cours uniquement (examen de fin de cours, pas les quiz/TD associés à une leçon).
     * Utilisé pour la liste "Évaluations" du dash instructeur (quiz associé à une leçon → liste Quiz, pas ici).
     */
    @Query("SELECT DISTINCT e FROM Evaluations e " +
           "LEFT JOIN FETCH e.course course2 " +
           "LEFT JOIN FETCH e.instructor " +
           "WHERE e.lesson IS NULL")
    List<Evaluations> findAllCourseLevelWithCourseAndInstructor();
}
