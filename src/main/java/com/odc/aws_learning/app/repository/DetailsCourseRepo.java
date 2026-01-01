package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.DetailsCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DetailsCourseRepo extends JpaRepository<DetailsCourse, Long> {

    Optional<DetailsCourse> findByCourseIdAndLearnerId(Long courseId, Long learnerId);

    @Query("SELECT COUNT(DISTINCT dc.learner) FROM DetailsCourse dc WHERE dc.course.instructor.id = :instructorId")
    Long countDistinctLearnersByInstructorCourses(@Param("instructorId") Long instructorId);
}
