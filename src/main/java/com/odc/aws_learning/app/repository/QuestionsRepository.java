package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
