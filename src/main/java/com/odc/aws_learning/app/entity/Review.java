package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    private Integer rating; // 1 to 5 stars

    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses course;

    private LocalDateTime createdAt;

    // NoArgsConstructor
    public Review() {
    }

    // AllArgsConstructor
    public Review(Integer rating, String comment, User user, Courses course, LocalDateTime createdAt) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Review review = (Review) o;
        return Objects.equals(rating, review.rating) &&
               Objects.equals(comment, review.comment) &&
               Objects.equals(user, review.user) &&
               Objects.equals(course, review.course) &&
               Objects.equals(createdAt, review.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, comment, user, course, createdAt);
    }
}