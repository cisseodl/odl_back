package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
