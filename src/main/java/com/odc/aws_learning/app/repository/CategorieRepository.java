package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
}
