package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Rubrique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubriqueRepository extends JpaRepository<Rubrique, Long> {
}
