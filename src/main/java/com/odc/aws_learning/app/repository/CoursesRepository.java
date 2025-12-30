package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Page<Courses> findAllByActivate(boolean activate, Pageable pageable);
    List<Courses> findByCategorieId(Long catId);
}
