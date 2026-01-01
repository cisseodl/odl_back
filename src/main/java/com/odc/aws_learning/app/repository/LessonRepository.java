package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleId(Long moduleId);
    Optional<Lesson> findByActivateAndId(boolean activate, Long id);
}