package com.odc.aws_learning.auth.repository;

import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findAllByActivateAndAdmin(boolean activate, boolean admin, Pageable pageable);
    Optional<User> findByPhone(String phone); // Added
    
    // Méthode avec JOIN FETCH pour charger les relations nécessaires (admin, instructor, apprenant, certificates)
    // Note: On ne peut pas FETCH plusieurs collections simultanément (MultipleBagFetchException)
    // On charge seulement admin, instructor, apprenant, les certificats seront chargés séparément si nécessaire
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.admin " +
           "LEFT JOIN FETCH u.instructor " +
           "LEFT JOIN FETCH u.apprenant")
    List<User> findAllWithRelations();
    
    // Méthode avec JOIN FETCH pour charger les relations avec pagination
    // On charge admin, instructor, apprenant (OneToOne) mais pas les collections pour éviter MultipleBagFetchException
    @Query(value = "SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.admin " +
           "LEFT JOIN FETCH u.instructor " +
           "LEFT JOIN FETCH u.apprenant",
           countQuery = "SELECT COUNT(DISTINCT u) FROM User u")
    org.springframework.data.domain.Page<User> findAllWithRelations(Pageable pageable);
}