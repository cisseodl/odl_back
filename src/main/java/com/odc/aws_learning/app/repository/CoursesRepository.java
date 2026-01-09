package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.app.constante.CourseStatus;
import com.odc.aws_learning.app.entity.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Page<Courses> findAllByActivate(boolean activate, Pageable pageable);
    Page<Courses> findByCategorieId(Long categoryId, Pageable pageable); // Modified
    Page<Courses> findByLevel(CourseLevel level, Pageable pageable);
    Page<Courses> findByBestseller(Boolean bestseller, Pageable pageable);
    long countByInstructorId(Long instructorId);

    // New method for AnalyticsService
    List<Courses> findByInstructor_Id(Long instructorId);

    long countByInstructorIdAndStatus(Long instructorId, CourseStatus status);

    long countByCreatedAtAfter(LocalDateTime date);

    long countByStatus(CourseStatus status);

    Page<Courses> findByStatus(CourseStatus status, Pageable pageable);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBefore(LocalDateTime date);
}