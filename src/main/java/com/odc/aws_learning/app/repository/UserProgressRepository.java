package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserAndLesson(User user, Lesson lesson);
    List<UserProgress> findByUserId(Long userId);
    List<UserProgress> findByUserIdAndLessonModuleCourseId(Long userId, Long courseId);

    // Méthode manquante
    @Query("SELECT COUNT(DISTINCT up.user) FROM UserProgress up WHERE up.lesson.module.course = :course")
    long countDistinctUsersByCourse(@Param("course") Courses course);
}