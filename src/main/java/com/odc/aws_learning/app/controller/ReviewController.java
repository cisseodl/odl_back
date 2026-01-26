package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.ReviewResponseDto; // Import new DTO
import com.odc.aws_learning.app.service.ReviewService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Keep this import as it's used by CResponse<List<ReviewResponseDto>>

@RequestMapping("/api/courses")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{courseId}/reviews")
    public ResponseEntity<CResponse<ReviewResponseDto>> addReview(@PathVariable Long courseId, // Changed return type
                                  @RequestParam Integer rating,
                                  @RequestParam String comment,
                                  @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return new ResponseEntity<>(CResponse.error("User not authenticated"), HttpStatus.UNAUTHORIZED); // Changed to ResponseEntity
        }
        CResponse<ReviewResponseDto> response = reviewService.addReview(courseId, currentUser.getId(), rating, comment); // Changed return type
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST); // Changed to ResponseEntity and using isSuccess
    }

    @GetMapping("/{courseId}/reviews")
    public ResponseEntity<CResponse<List<ReviewResponseDto>>> getReviewsByCourse(@PathVariable Long courseId) { // Changed return type
        CResponse<List<ReviewResponseDto>> response = reviewService.getReviewsByCourse(courseId); // Changed return type
        return new ResponseEntity<>(response, HttpStatus.OK); // Changed to ResponseEntity
    }

    @GetMapping("/reviews/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<List<ReviewResponseDto>>> getAllReviews() { // Changed return type
        CResponse<List<ReviewResponseDto>> response = reviewService.getAllReviews(); // Changed return type
        return new ResponseEntity<>(response, HttpStatus.OK); // Changed to ResponseEntity
    }

    // New DELETE endpoint for reviews
    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can delete reviews
    public ResponseEntity<CResponse<?>> deleteReview(@PathVariable Long reviewId) {
        CResponse<?> response = reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND); // NOT_FOUND if review not found
    }
}
