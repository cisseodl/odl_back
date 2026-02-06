package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourseId(Long courseId);
    List<Review> findByCourseIdAndActivateIsTrue(Long courseId);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user LEFT JOIN FETCH r.course WHERE r.course.id = :courseId AND r.activate = true")
    List<Review> findByCourseIdAndActivateIsTrueWithUserAndCourse(@Param("courseId") Long courseId);
    List<Review> findByUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.instructor.id = :instructorId")
    Double findAverageRatingByInstructorCourses(@Param("instructorId") Long instructorId);

    // Méthodes manquantes
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course = :course")
    Double findAverageRatingByCourse(@Param("course") Courses course);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.course = :course")
    Long countByCourse(@Param("course") Courses course);

    long countByCourse_Instructor_IdAndCreatedAtAfter(Long instructorId, LocalDateTime date);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = :courseId AND r.createdAt BETWEEN :start AND :end")
    Double findAverageRatingByCourseIdAndCreatedAtBetween(@Param("courseId") Long courseId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // Find overall average rating across all courses
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.activate = true")
    Double findOverallAverageRating();

    Optional<Review> findByCourseIdAndUserId(Long courseId, Long userId);
}