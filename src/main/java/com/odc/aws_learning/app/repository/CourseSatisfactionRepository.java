package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.CourseSatisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseSatisfactionRepository extends JpaRepository<CourseSatisfaction, Long> {
    Optional<CourseSatisfaction> findByEvaluationAttemptId(Long evaluationAttemptId);
    Optional<CourseSatisfaction> findByCourseIdAndUserId(Long courseId, Long userId);
}
