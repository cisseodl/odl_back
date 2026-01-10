package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByActivateAndIdAndCourseId(boolean activate, Long id, Long course_id);
    List<Module> findAllByActivateAndCourseId(boolean activate, Long course_id);
    List<Module> findByCourseId(Long courseId);
    
    @Query("SELECT DISTINCT m FROM Module m LEFT JOIN FETCH m.lessons l WHERE m.course.id = :courseId AND m.activate = true AND (l.activate = true OR l.activate IS NULL)")
    List<Module> findAllByActivateAndCourseIdWithLessons(@Param("courseId") Long courseId);
}