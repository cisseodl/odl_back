package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Cohorte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CohorteRepository extends JpaRepository<Cohorte, Long> {
}
