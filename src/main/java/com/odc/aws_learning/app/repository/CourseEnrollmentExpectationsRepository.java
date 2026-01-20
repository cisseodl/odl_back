package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.CourseEnrollmentExpectations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseEnrollmentExpectationsRepository extends JpaRepository<CourseEnrollmentExpectations, Long> {
    Optional<CourseEnrollmentExpectations> findByDetailsCourseId(Long detailsCourseId);
}
