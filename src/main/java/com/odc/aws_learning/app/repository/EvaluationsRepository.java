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
     * Examen de fin de cours : évaluation de type QUIZ sans leçon associée (certification).
     * À ne pas confondre avec les TD (type TP, associés à une leçon).
     */
    @Query("SELECT e FROM Evaluations e WHERE e.course.id = :courseId AND e.type = 'QUIZ' AND e.lesson IS NULL")
    List<Evaluations> findCourseExamsByCourseId(@Param("courseId") Long courseId);
    
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
}
