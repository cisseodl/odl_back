package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.app.constante.CourseStatus;
import com.odc.aws_learning.app.entity.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Page<Courses> findAllByActivate(boolean activate, Pageable pageable);
    Page<Courses> findByCategorieId(Long categoryId, Pageable pageable); // Modified
    Page<Courses> findByLevel(CourseLevel level, Pageable pageable);
    Page<Courses> findByBestseller(Boolean bestseller, Pageable pageable);
    long countByInstructorId(Long instructorId);

    // New method for AnalyticsService
    List<Courses> findByInstructor_Id(Long instructorId);
    
    // Méthode avec jointures FETCH pour charger les relations nécessaires pour un instructeur
    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.instructor.id = :instructorId")
    List<Courses> findByInstructor_IdWithRelations(@Param("instructorId") Long instructorId);

    /** Cours de l'instructeur OU cours sans formateur (instructor_id null), pour permettre à un instructeur de s'assigner après import / vidage DB */
    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE (c.instructor.id = :instructorId OR c.instructor IS NULL)")
    List<Courses> findByInstructor_IdOrInstructorNullWithRelations(@Param("instructorId") Long instructorId);

    long countByInstructorIdAndStatus(Long instructorId, CourseStatus status);

    long countByCreatedAtAfter(LocalDateTime date);

    long countByStatus(CourseStatus status);

    Page<Courses> findByStatus(CourseStatus status, Pageable pageable);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBefore(LocalDateTime date);

    // Méthodes avec jointures FETCH pour charger les relations nécessaires
    // Note: On ne peut pas FETCH plusieurs collections simultanément (MultipleBagFetchException)
    // On charge seulement instructor, formation, categorie (via formation ou directement)
    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie")
    List<Courses> findAllWithRelations();

    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.status = :status")
    List<Courses> findByStatusWithRelations(@Param("status") CourseStatus status);

    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.status IN :statuses")
    List<Courses> findByStatusInWithRelations(@Param("statuses") java.util.List<CourseStatus> statuses);

    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.categorie.id = :categoryId")
    List<Courses> findByCategorieIdWithRelations(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.level = :level")
    List<Courses> findByLevelWithRelations(@Param("level") CourseLevel level);

    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.bestseller = :bestseller")
    List<Courses> findByBestsellerWithRelations(@Param("bestseller") Boolean bestseller);
    
    // Méthode pour charger un cours avec toutes ses relations nécessaires (catégorie, formation, instructeur)
    @Query("SELECT DISTINCT c FROM Courses c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.categorie " +
           "WHERE c.id = :id")
    java.util.Optional<Courses> findByIdWithRelations(@Param("id") Long id);
}