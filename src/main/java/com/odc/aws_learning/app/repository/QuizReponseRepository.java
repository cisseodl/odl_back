package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.QuizReponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizReponseRepository extends JpaRepository<QuizReponse, Long> {
    List<QuizReponse> findByQuestionId(Long questionId);
    List<QuizReponse> findByQuestionIdAndEstCorrecteTrue(Long questionId);
}
