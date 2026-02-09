package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.ReviewResponseDto; // Import new DTO
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Review;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for transactional operation

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // For Collectors.toList()

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CoursesRepository coursesRepository;
    private final UserRepository userRepository;
    private final DetailsCourseRepo detailsCourseRepo;

    public ReviewService(ReviewRepository reviewRepository, CoursesRepository coursesRepository, UserRepository userRepository, DetailsCourseRepo detailsCourseRepo) {
        this.reviewRepository = reviewRepository;
        this.coursesRepository = coursesRepository;
        this.userRepository = userRepository;
        this.detailsCourseRepo = detailsCourseRepo;
    }

    @Transactional
    public CResponse<ReviewResponseDto> addReview(Long courseId, User currentUser, Integer rating, String comment) {
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }

        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return CResponse.error("Cours non trouvé");
        }

        // Vérifier que l'utilisateur est inscrit au cours
        Optional<com.odc.aws_learning.app.entity.DetailsCourse> enrollment = detailsCourseRepo
                .findByCourseIdAndLearnerId(courseId, currentUser.getId());
        
        if (enrollment.isEmpty() || !enrollment.get().isActivate()) {
            return CResponse.error("Vous devez être inscrit à ce cours pour pouvoir donner un avis");
        }

        // Vérifier si l'utilisateur a déjà donné un avis pour ce cours
        Optional<Review> existingReview = reviewRepository.findByCourseIdAndUserId(courseId, currentUser.getId());
        if (existingReview.isPresent()) {
            return CResponse.error("Vous avez déjà donné un avis pour ce cours");
        }

        Review review = new Review();
        review.setCourse(courseOptional.get());
        review.setUser(currentUser);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setActivate(true);

        Review savedReview = reviewRepository.save(review);
        return CResponse.success(mapToReviewResponseDto(savedReview), "Avis ajouté avec succès");
    }

    public CResponse<List<ReviewResponseDto>> getReviewsByCourse(Long courseId) { // Changed return type
        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return CResponse.error("Course not found");
        }
        List<Review> reviews = reviewRepository.findByCourseIdAndActivateIsTrueWithUserAndCourse(courseId);
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                                                    .map(this::mapToReviewResponseDto)
                                                    .collect(Collectors.toList());
        return CResponse.success(reviewDtos, "Reviews fetched successfully");
    }

    public CResponse<List<ReviewResponseDto>> getAllReviews() { // Changed return type
        List<Review> reviews = reviewRepository.findAllWithUserAndCourse();
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                                                    .map(this::mapToReviewResponseDto)
                                                    .collect(Collectors.toList());
        return CResponse.success(reviewDtos, "All reviews fetched successfully");
    }

    @Transactional // Ensure the operation is transactional
    public CResponse<?> deleteReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            return CResponse.error("Review not found with ID: " + reviewId);
        }

        reviewRepository.deleteById(reviewId);
        return CResponse.success(null, "Review with ID: " + reviewId + " deleted successfully.");
    }

    // New mapping method
    private ReviewResponseDto mapToReviewResponseDto(Review review) {
        ReviewResponseDto.ReviewUserInfo userInfo = null;
        if (review.getUser() != null) {
            userInfo = new ReviewResponseDto.ReviewUserInfo(
                    review.getUser().getId(),
                    review.getUser().getFullName(),
                    review.getUser().getEmail(),
                    review.getUser().getAvatar()
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
