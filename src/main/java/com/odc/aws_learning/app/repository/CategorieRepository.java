package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByTitle(String title);
    
    /**
     * Compte le nombre de cours associés à une catégorie
     */
    @Query("SELECT COUNT(c) FROM Courses c WHERE c.categorie.id = :categoryId")
    long countCoursesByCategoryId(@Param("categoryId") Long categoryId);
}