package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.LearnerChapter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerChapterRepository extends JpaRepository<LearnerChapter, Long> {
}
