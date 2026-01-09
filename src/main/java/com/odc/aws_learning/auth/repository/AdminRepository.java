package com.odc.aws_learning.auth.repository;

import com.odc.aws_learning.auth.entities.Admin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserId(Long userId);
    
    // Méthode avec EntityGraph pour charger la relation user de manière eager
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT a FROM Admin a")
    List<Admin> findAllWithUser();
    
    // Méthode avec JOIN FETCH pour charger la relation user
    @Query("SELECT DISTINCT a FROM Admin a LEFT JOIN FETCH a.user")
    List<Admin> findAllWithUserJoinFetch();
    
    // Méthode pour charger un Admin avec son User par ID
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT a FROM Admin a WHERE a.id = :id")
    Optional<Admin> findByIdWithUser(Long id);
}
