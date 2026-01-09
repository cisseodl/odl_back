package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime; // Keep only if other LocalDateTime fields are used
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    private Integer rating; // 1 to 5 stars

    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference // Added (assuming User has a List<Review>)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference // Added (assuming Courses has a List<Review>)
    private Courses course;

    // Removed redundant createdAt field - inherited from BaseEntity
    // Removed @PrePersist method - BaseEntity @CreatedDate handles it

    // NoArgsConstructor
    public Review() {
        super();
    }

    // AllArgsConstructor
    public Review(Integer rating, String comment, User user, Courses course) { // Removed createdAt from constructor
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        // this.createdAt = createdAt; // Inherited from BaseEntity
    }

    // Getters
    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public Courses getCourse() {
        return course;
    }

    // Removed getCreatedAt() - inherited from BaseEntity
    // Use getCreatedAt() from BaseEntity instead

    // Setters
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    // Removed setCreatedAt() - inherited from BaseEntity

    // Removed @PrePersist method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Review review = (Review) o;
        return Objects.equals(rating, review.rating) &&
               Objects.equals(comment, review.comment) &&
               Objects.equals(user, review.user) &&
               Objects.equals(course, review.course);
               // Objects.equals(createdAt, review.createdAt); // Inherited from BaseEntity
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, comment, user, course); // Removed createdAt
    }

    @Override
    public String toString() {
        return "Review{" +
               "rating=" + rating +
               ", comment='" + comment + '\'' +
               ", user=" + (user != null ? user.getId() : "null") +
               ", course=" + (course != null ? course.getId() : "null") +
               ", createdAt=" + getCreatedAt() + // Use inherited method
               ", id=" + id +
               '}';
    }
}