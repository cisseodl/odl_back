package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.ReviewService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/courses")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{courseId}/reviews")
    public CResponse<?> addReview(@PathVariable Long courseId,
                                  @RequestParam Integer rating,
                                  @RequestParam String comment,
                                  @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return CResponse.error("User not authenticated");
        }
        return reviewService.addReview(courseId, currentUser.getId(), rating, comment);
    }

    @GetMapping("/{courseId}/reviews")
    public CResponse<?> getReviewsByCourse(@PathVariable Long courseId) {
        return reviewService.getReviewsByCourse(courseId);
    }

    @GetMapping("/reviews/all")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getAllReviews() {
        return reviewService.getAllReviews();
    }
}
