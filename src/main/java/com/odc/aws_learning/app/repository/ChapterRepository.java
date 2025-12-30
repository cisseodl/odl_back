package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByActivateAndIdAndCourseId(boolean activate, Long id, Long course_id);
    List<Chapter> findAllByActivateAndCourseId(boolean activate, Long course_id);
}
