package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "user_progress")
public class UserProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference // Added (assuming User has a List<UserProgress>)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonBackReference // Added (assuming Lesson has a List<UserProgress>)
    private Lesson lesson;

    private LocalDateTime completedAt; // Kept as it's semantically different from createdAt

    // NoArgsConstructor
    public UserProgress() {
        super();
    }

    // AllArgsConstructor
    public UserProgress(User user, Lesson lesson, LocalDateTime completedAt) {
        this.user = user;
        this.lesson = lesson;
        this.completedAt = completedAt;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    // Removed @PrePersist method as completedAt should be explicitly set or handled by service

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserProgress that = (UserProgress) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(lesson, that.lesson) &&
               Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, lesson, completedAt);
    }

    @Override
    public String toString() {
        return "UserProgress{" +
               "user=" + (user != null ? user.getId() : "null") +
               ", lesson=" + (lesson != null ? lesson.getId() : "null") +
               ", completedAt=" + completedAt +
               ", id=" + id +
               '}';
    }
}