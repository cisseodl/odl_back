package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.app.entity.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Page<Courses> findAllByActivate(boolean activate, Pageable pageable);
    Page<Courses> findByCategorieId(Long categoryId, Pageable pageable); // Modified
    Page<Courses> findByLevel(CourseLevel level, Pageable pageable);
    Page<Courses> findByBestseller(Boolean bestseller, Pageable pageable);
    long countByInstructorId(Long instructorId);
}