package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Review;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CoursesRepository coursesRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, CoursesRepository coursesRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.coursesRepository = coursesRepository;
        this.userRepository = userRepository;
    }

    public CResponse<?> addReview(Long courseId, Long userId, Integer rating, String comment) {
        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (courseOptional.isEmpty()) {
            return CResponse.error("Course not found");
        }
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }

        Review review = new Review();
        review.setCourse(courseOptional.get());
        review.setUser(userOptional.get());
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now()); // Ensure this is set or @PrePersist handles it.

        reviewRepository.save(review);
        return CResponse.success(review, "Review added successfully");
    }

    public CResponse<?> getReviewsByCourse(Long courseId) {
        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return CResponse.error("Course not found");
        }
        List<Review> reviews = reviewRepository.findByCourseIdAndActivateIsTrue(courseId);
        return CResponse.success(reviews, "Reviews fetched successfully");
    }

    public CResponse<?> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return CResponse.success(reviews, "All reviews fetched successfully");
    }
}
