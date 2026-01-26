package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.ReviewResponseDto; // Import new DTO
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
import java.util.stream.Collectors; // For Collectors.toList()

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

    public CResponse<ReviewResponseDto> addReview(Long courseId, Long userId, Integer rating, String comment) { // Changed return type
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
        review.setCreatedAt(LocalDateTime.now()); 

        Review savedReview = reviewRepository.save(review);
        return CResponse.success(mapToReviewResponseDto(savedReview), "Review added successfully"); // Map to DTO
    }

    public CResponse<List<ReviewResponseDto>> getReviewsByCourse(Long courseId) { // Changed return type
        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return CResponse.error("Course not found");
        }
        List<Review> reviews = reviewRepository.findByCourseIdAndActivateIsTrue(courseId);
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                                                    .map(this::mapToReviewResponseDto)
                                                    .collect(Collectors.toList());
        return CResponse.success(reviewDtos, "Reviews fetched successfully");
    }

    public CResponse<List<ReviewResponseDto>> getAllReviews() { // Changed return type
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                                                    .map(this::mapToReviewResponseDto)
                                                    .collect(Collectors.toList());
        return CResponse.success(reviewDtos, "All reviews fetched successfully");
    }

    // New mapping method
    private ReviewResponseDto mapToReviewResponseDto(Review review) {
        ReviewResponseDto.ReviewUserInfo userInfo = null;
        if (review.getUser() != null) {
            userInfo = new ReviewResponseDto.ReviewUserInfo(
                    review.getUser().getId(),
                    review.getUser().getFullName(),
                    review.getUser().getEmail()
            );
        }

        ReviewResponseDto.ReviewCourseInfo courseInfo = null;
        if (review.getCourse() != null) {
            courseInfo = new ReviewResponseDto.ReviewCourseInfo(
                    review.getCourse().getId(),
                    review.getCourse().getTitle()
            );
        }

        return new ReviewResponseDto(
                review.getId(),
                review.getRating(),
                review.getComment(),
                userInfo,
                courseInfo,
                review.getCreatedAt()
        );
    }
}
