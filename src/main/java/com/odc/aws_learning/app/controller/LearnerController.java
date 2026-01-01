package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.LearnerLessonService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class LearnerController {

    private final LearnerLessonService learnerLessonService;

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
}
