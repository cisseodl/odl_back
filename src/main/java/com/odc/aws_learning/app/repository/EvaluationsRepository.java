package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Evaluations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationsRepository extends JpaRepository<Evaluations, Long> {
    @Query("SELECT e FROM Evaluations e WHERE e.course.id = :courseId")
    List<Evaluations> findByCourseId(@Param("courseId") Long courseId);
}
