package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.CourseFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseFeedbackRepository extends JpaRepository<CourseFeedback, Long> {
    Optional<CourseFeedback> findByDetailsCourseId(Long detailsCourseId);
    Optional<CourseFeedback> findByExamAttemptId(Long examAttemptId);
}
