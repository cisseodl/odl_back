package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.LearnerLessonService;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class LearnerController {

    private final LearnerLessonService learnerLessonService;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<CResponse<?>> completeLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        CResponse<?> response = learnerLessonService.completeLesson(courseId, lessonId, currentUser);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(400).body(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CResponse<?>> getCourseProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        CResponse<?> response = learnerLessonService.getCourseProgress(courseId, currentUser);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(response);
    }

    /**
     * Récupère l'activité récente de l'utilisateur (dernières leçons complétées)
     * GET /api/learn/recent-activity
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<CResponse<?>> getRecentActivity(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "3") int limit) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        
        try {
            // Récupérer les dernières leçons complétées, triées par date de complétion décroissante
            List<UserProgress> allProgress = userProgressRepository.findByUserId(currentUser.getId());
            List<UserProgress> recentProgress = allProgress.stream()
                .filter(up -> up.getCompletedAt() != null)
                .sorted((a, b) -> {
                    if (a.getCompletedAt() == null && b.getCompletedAt() == null) return 0;
                    if (a.getCompletedAt() == null) return 1;
                    if (b.getCompletedAt() == null) return -1;
                    return b.getCompletedAt().compareTo(a.getCompletedAt());
                })
                .limit(limit)
                .collect(Collectors.toList());

            List<Map<String, Object>> activityList = recentProgress.stream().map(up -> {
                Map<String, Object> activityMap = new HashMap<>();
                if (up.getLesson() != null && up.getLesson().getModule() != null && 
                    up.getLesson().getModule().getCourse() != null) {
                    activityMap.put("courseId", up.getLesson().getModule().getCourse().getId());
                    activityMap.put("courseTitle", up.getLesson().getModule().getCourse().getTitle());
                    activityMap.put("lessonTitle", up.getLesson().getTitle());
                    activityMap.put("completedAt", up.getCompletedAt());
                    // Calculer le pourcentage de progression du cours
                    Long courseId = up.getLesson().getModule().getCourse().getId();
                    long totalLessons = lessonRepository.countByModule_Course_Id(courseId);
                    long completedLessons = userProgressRepository
                        .findByUserIdAndLessonModuleCourseId(currentUser.getId(), courseId).size();
                    int progress = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;
                    activityMap.put("progress", progress);
                }
                return activityMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(CResponse.success(activityList, "Activité récente récupérée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(CResponse.error("Erreur lors de la récupération de l'activité récente: " + e.getMessage()));
        }
    }
}
