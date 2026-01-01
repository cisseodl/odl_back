package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourseId(Long courseId);
    List<Review> findByCourseIdAndActivateIsTrue(Long courseId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.instructor.id = :instructorId")
    Double findAverageRatingByInstructorCourses(@Param("instructorId") Long instructorId);

    // Méthodes manquantes
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course = :course")
    Double findAverageRatingByCourse(@Param("course") Courses course);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.course = :course")
    Long countByCourse(@Param("course") Courses course);
}