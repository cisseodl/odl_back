package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/details-course")
@RequiredArgsConstructor
public class DetailsCourseController {

    private final DetailsCourseRepo detailsCourseRepo;

    /**
     * Récupère tous les détails d'inscription pour un cours spécifique
     * GET /details-course/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getDetailsByCourseId(@PathVariable Long courseId) {
        try {
            List<DetailsCourse> detailsCourses = detailsCourseRepo.findAllByCourseId(courseId);
            
            // Mapper les entités vers des Maps pour éviter les problèmes de sérialisation JSON
            List<Map<String, Object>> detailsList = detailsCourses.stream().map(dc -> {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("id", dc.getId());
                detailMap.put("courseId", dc.getCourse() != null ? dc.getCourse().getId() : null);
                detailMap.put("learnerId", dc.getLearner() != null ? dc.getLearner().getId() : null);
                detailMap.put("learnerName", dc.getLearner() != null ? 
                    (dc.getLearner().getFullName() != null ? dc.getLearner().getFullName() : dc.getLearner().getEmail()) : null);
                detailMap.put("learnerEmail", dc.getLearner() != null ? dc.getLearner().getEmail() : null);
                detailMap.put("courseStatut", dc.getCourseStatut() != null ? dc.getCourseStatut().name() : null);
                detailMap.put("completed", dc.isCompleted());
                detailMap.put("activate", dc.isActivate());
                detailMap.put("createdAt", dc.getCreatedAt());
                detailMap.put("lastModifiedAt", dc.getLastModifiedAt());
                return detailMap;
            }).collect(Collectors.toList());
            
            return CResponse.success(detailsList, "Détails du cours récupérés avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des détails du cours: " + e.getMessage());
        }
    }

    /**
     * Récupère tous les cours auxquels un utilisateur est inscrit
     * GET /details-course/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getEnrollmentsByUserId(@PathVariable Long userId) {
        try {
            List<DetailsCourse> detailsCourses = detailsCourseRepo.findByLearnerId(userId);
            
            // Mapper les entités vers des Maps pour éviter les problèmes de sérialisation JSON
            List<Map<String, Object>> enrollmentsList = detailsCourses.stream().map(dc -> {
                Map<String, Object> enrollmentMap = new HashMap<>();
                enrollmentMap.put("id", dc.getId());
                enrollmentMap.put("courseId", dc.getCourse() != null ? dc.getCourse().getId() : null);
                enrollmentMap.put("courseTitle", dc.getCourse() != null ? dc.getCourse().getTitle() : null);
                enrollmentMap.put("learnerId", dc.getLearner() != null ? dc.getLearner().getId() : null);
                enrollmentMap.put("courseStatut", dc.getCourseStatut() != null ? dc.getCourseStatut().name() : null);
                enrollmentMap.put("completed", dc.isCompleted());
                enrollmentMap.put("activate", dc.isActivate());
                enrollmentMap.put("createdAt", dc.getCreatedAt());
                enrollmentMap.put("lastModifiedAt", dc.getLastModifiedAt());
                return enrollmentMap;
            }).collect(Collectors.toList());
            
            return CResponse.success(enrollmentsList, "Cours de l'utilisateur récupérés avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des cours de l'utilisateur: " + e.getMessage());
        }
    }
}
