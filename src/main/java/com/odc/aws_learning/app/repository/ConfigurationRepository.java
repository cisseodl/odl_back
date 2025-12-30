package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    // Méthode pour récupérer la dernière configuration
    Configuration findTopByOrderByIdDesc();
}
