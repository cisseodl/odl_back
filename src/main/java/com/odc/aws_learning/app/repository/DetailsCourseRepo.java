package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.DetailsCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.odc.aws_learning.app.constante.Enumeration;
import com.odc.aws_learning.app.dto.UserCourseCompletionStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DetailsCourseRepo extends JpaRepository<DetailsCourse, Long> {

    Optional<DetailsCourse> findByCourseIdAndLearnerId(Long courseId, Long learnerId);

    long countByLearnerId(Long learnerId);

    long countByLearnerIdAndCourseStatut(Long learnerId, Enumeration.COURSE_STATUT courseStatut);

    @Query("SELECT COUNT(DISTINCT dc.learner) FROM DetailsCourse dc WHERE dc.course.instructor.id = :instructorId")
    Long countDistinctLearnersByInstructorCourses(@Param("instructorId") Long instructorId);

    @Query("SELECT new com.odc.aws_learning.app.dto.UserCourseCompletionStats(dc.learner.id, COUNT(dc.id)) FROM DetailsCourse dc WHERE dc.completed = true GROUP BY dc.learner.id")
    List<UserCourseCompletionStats> findUserCourseCompletionStats();

    long countByCourseId(Long courseId); // Added
    long countByCourseIdAndCourseStatut(Long courseId, Enumeration.COURSE_STATUT courseStatut); // Added
    List<DetailsCourse> findByLearnerId(Long learnerId); // Added
    List<DetailsCourse> findByLearnerIdAndCourseStatut(Long learnerId, Enumeration.COURSE_STATUT courseStatut); // Added

    long countByCourse_Instructor_Id(Long instructorId);

    long countByCourse_Instructor_IdAndCreatedAtAfter(Long instructorId, LocalDateTime date);

    @Query("SELECT COUNT(DISTINCT dc.learner.id) FROM DetailsCourse dc WHERE dc.course.instructor.id = :instructorId AND dc.lastModifiedAt > :date")
    long countDistinctLearnerByCourse_Instructor_IdAndLastModifiedAtAfter(@Param("instructorId") Long instructorId, @Param("date") LocalDateTime date);

    long countByCourse_Instructor_IdAndCompleted(Long instructorId, boolean completed);

    @Query(value = "SELECT c.title, COUNT(dc.id) as enrollment_count FROM details_course dc JOIN courses c ON dc.course_id = c.id GROUP BY c.title ORDER BY enrollment_count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5CoursesByEnrollments();

    List<DetailsCourse> findAllByCourseId(Long courseId);

    long countByLearner_IdAndCompleted(Long learnerId, boolean completed);

    long countByCreatedAtAfter(LocalDateTime date);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByCompletedTrueAndCreatedAtAfter(LocalDateTime date);
    long countByCompletedTrueAndCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByCourseIdAndCreatedAtBetween(Long courseId, LocalDateTime start, LocalDateTime end);
    long countByCourseIdAndCompletedTrueAndCreatedAtBetween(Long courseId, LocalDateTime start, LocalDateTime end);
}
