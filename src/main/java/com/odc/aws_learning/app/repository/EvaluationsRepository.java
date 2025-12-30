package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Evaluations;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EvaluationsRepository extends JpaRepository<Evaluations, Long> {
}
