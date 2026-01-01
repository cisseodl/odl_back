package com.odc.aws_learning.app.repository;


import com.odc.aws_learning.app.entity.LearnerModule;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerModuleRepository extends JpaRepository<LearnerModule, Long> {
}
