package com.odc.aws_learning.auth.repository;

import com.odc.aws_learning.auth.entities.Instructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByUserId(Long userId);
    
    // Méthode avec EntityGraph pour charger la relation user de manière eager
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT i FROM Instructor i")
    List<Instructor> findAllWithUser();
    
    // Méthode avec JOIN FETCH pour charger la relation user
    @Query("SELECT DISTINCT i FROM Instructor i LEFT JOIN FETCH i.user")
    List<Instructor> findAllWithUserJoinFetch();
}
