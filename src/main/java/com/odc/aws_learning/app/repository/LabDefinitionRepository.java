package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.LabDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des définitions de labs.
 */
@Repository
public interface LabDefinitionRepository extends JpaRepository<LabDefinition, Long> {

    List<LabDefinition> findByActivateTrue();

    /**
     * Récupère tous les labs avec leurs leçons, modules et cours chargés
     */
    @Query("SELECT DISTINCT l FROM LabDefinition l " +
           "LEFT JOIN FETCH l.lesson lesson " +
           "LEFT JOIN FETCH lesson.module module " +
           "LEFT JOIN FETCH module.course course")
    List<LabDefinition> findAllWithLessonModuleAndCourse();

    /**
     * Labs actifs du cours (via lesson.module.course), avec la leçon chargée pour grouper par lessonId.
     */
    @Query("SELECT DISTINCT l FROM LabDefinition l LEFT JOIN FETCH l.lesson WHERE l.activate = true AND l.lesson IS NOT NULL AND l.lesson.module.course.id = :courseId")
    List<LabDefinition> findByCourseIdViaLesson(@Param("courseId") Long courseId);
}
