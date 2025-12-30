package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.DetailsCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailsCourseRepository extends JpaRepository<DetailsCourse, Long> {

    Optional<DetailsCourse> findByCourseIdAndLearnerId(Long courseId, Long learnerId);
}
