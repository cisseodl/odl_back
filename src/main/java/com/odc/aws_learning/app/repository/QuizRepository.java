package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourseId(Long courseId);
    List<Quiz> findByCourseIdAndActivateTrue(Long courseId);

    /** Récupère les quiz du cours avec la leçon chargée (pour exposer lessonId côté apprenant). */
    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.lesson WHERE q.course.id = :courseId AND q.activate = true")
    List<Quiz> findByCourseIdAndActivateTrueWithLesson(@Param("courseId") Long courseId);
}
