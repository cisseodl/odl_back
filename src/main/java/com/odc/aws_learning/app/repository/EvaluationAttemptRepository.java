package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.EvaluationAttempt;
import com.odc.aws_learning.app.entity.Evaluations;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationAttemptRepository extends JpaRepository<EvaluationAttempt, Long> {
    
    List<EvaluationAttempt> findByEvaluation(Evaluations evaluation);
    
    List<EvaluationAttempt> findByUser(User user);
    
    List<EvaluationAttempt> findByEvaluationAndUser(Evaluations evaluation, User user);
    
    @Query("SELECT ea FROM EvaluationAttempt ea WHERE ea.evaluation.id = :evaluationId AND ea.user.id = :userId ORDER BY ea.createdAt DESC")
    List<EvaluationAttempt> findByEvaluationIdAndUserIdOrderByCreatedAtDesc(@Param("evaluationId") Long evaluationId, @Param("userId") Long userId);
    
    @Query("SELECT ea FROM EvaluationAttempt ea WHERE ea.evaluation.course.id = :courseId AND ea.user.id = :userId AND ea.status = 'PASSED'")
    List<EvaluationAttempt> findPassedAttemptsByCourseAndUser(@Param("courseId") Long courseId, @Param("userId") Long userId);
    
    @Query("SELECT MAX(ea.score) FROM EvaluationAttempt ea WHERE ea.evaluation.id = :evaluationId AND ea.user.id = :userId")
    Optional<Double> findMaxScoreByEvaluationAndUser(@Param("evaluationId") Long evaluationId, @Param("userId") Long userId);
    
    @Query("SELECT ea FROM EvaluationAttempt ea WHERE ea.evaluation.instructor.id = :instructorId AND ea.status = 'PENDING'")
    List<EvaluationAttempt> findPendingAttemptsByInstructor(@Param("instructorId") Long instructorId);
}
