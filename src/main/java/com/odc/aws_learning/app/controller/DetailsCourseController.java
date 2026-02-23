package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.constante.Enumeration;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/details-course")
@RequiredArgsConstructor
public class DetailsCourseController {

    private final DetailsCourseRepo detailsCourseRepo;
    private final CoursesRepository coursesRepository;
    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    /**
     * Récupère tous les détails d'inscription pour un cours spécifique
     * GET /details-course/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getDetailsByCourseId(@PathVariable Long courseId) {
        try {
            List<DetailsCourse> detailsCourses = detailsCourseRepo.findAllByCourseId(courseId);
            
            // Mapper les entités vers des Maps avec progression (leçons complétées / total)
            List<Map<String, Object>> detailsList = detailsCourses.stream().map(dc -> {
                Map<String, Object> detailMap = new HashMap<>();
                Long learnerId = dc.getLearner() != null ? dc.getLearner().getId() : null;
                Long cId = dc.getCourse() != null ? dc.getCourse().getId() : null;
                detailMap.put("id", dc.getId());
                detailMap.put("courseId", cId);
                detailMap.put("learnerId", learnerId);
                detailMap.put("learnerName", dc.getLearner() != null ? 
                    (dc.getLearner().getFullName() != null ? dc.getLearner().getFullName() : dc.getLearner().getEmail()) : null);
                detailMap.put("learnerEmail", dc.getLearner() != null ? dc.getLearner().getEmail() : null);
                detailMap.put("courseStatut", dc.getCourseStatut() != null ? dc.getCourseStatut().name() : null);
                detailMap.put("completed", dc.isCompleted());
                detailMap.put("activate", dc.isActivate());
                detailMap.put("createdAt", dc.getCreatedAt());
                detailMap.put("lastModifiedAt", dc.getLastModifiedAt());
                // Progression : leçons complétées / total leçons (pour le dash instructeur)
                if (learnerId != null && cId != null) {
                    long totalLessons = lessonRepository.countByModule_Course_Id(cId);
                    long completedLessons = userProgressRepository.findByUserIdAndLessonModuleCourseId(learnerId, cId).stream()
                            .filter(up -> up.getCompletedAt() != null).count();
                    int progress = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;
                    int totalModules = moduleRepository.findByCourseId(cId).size();
                    detailMap.put("progress", progress);
                    detailMap.put("completedLessons", (int) completedLessons);
                    detailMap.put("totalLessons", (int) totalLessons);
                    detailMap.put("completedModules", totalModules > 0 ? (int) Math.min((completedLessons * totalModules) / Math.max(1, totalLessons), totalModules) : 0);
                    detailMap.put("totalModules", totalModules);
                } else {
                    detailMap.put("progress", 0);
                    detailMap.put("completedLessons", 0);
                    detailMap.put("totalLessons", 0);
                    detailMap.put("completedModules", 0);
                    detailMap.put("totalModules", 0);
                }
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

    /**
     * Récupère les cours complétés de l'utilisateur authentifié avec leurs détails
     * GET /details-course/my-completed-courses
     */
    @GetMapping("/my-completed-courses")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<?> getMyCompletedCourses() {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return CResponse.error("Utilisateur non authentifié");
            }

            List<DetailsCourse> completedCourses = detailsCourseRepo.findByLearnerIdAndCourseStatut(
                currentUser.getId(), 
                Enumeration.COURSE_STATUT.Valide
            );

            List<Map<String, Object>> coursesList = completedCourses.stream()
                .filter(DetailsCourse::isCompleted)
                .map(dc -> {
                    Map<String, Object> courseMap = new HashMap<>();
                    if (dc.getCourse() != null) {
                        courseMap.put("id", dc.getCourse().getId());
                        courseMap.put("title", dc.getCourse().getTitle());
                        courseMap.put("description", dc.getCourse().getDescription());
                        courseMap.put("imageUrl", dc.getCourse().getImagePath());
                        courseMap.put("duration", dc.getCourse().getDuration());
                        courseMap.put("level", dc.getCourse().getLevel() != null ? dc.getCourse().getLevel().name() : null);
                        courseMap.put("category", dc.getCourse().getCategorie() != null ? dc.getCourse().getCategorie().getTitle() : null);
                        courseMap.put("completedAt", dc.getLastModifiedAt());
                        courseMap.put("instructor", dc.getCourse().getInstructor() != null ? 
                            (dc.getCourse().getInstructor().getFullName() != null ? 
                                dc.getCourse().getInstructor().getFullName() : 
                                dc.getCourse().getInstructor().getEmail()) : null);
                    }
                    return courseMap;
                })
                .collect(Collectors.toList());

            return CResponse.success(coursesList, "Cours complétés récupérés avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des cours complétés: " + e.getMessage());
        }
    }

    /**
     * Récupère l'utilisateur actuellement authentifié
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.orElse(null);
        }
        return null;
    }
}
