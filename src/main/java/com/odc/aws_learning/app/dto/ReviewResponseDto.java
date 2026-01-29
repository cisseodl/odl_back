package com.odc.aws_learning.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    private Long id;
    private Integer rating;
    private String comment;
    private ReviewUserInfo user; // Simplified user info
    private ReviewCourseInfo course; // Simplified course info
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public ReviewResponseDto() {
    }

    public ReviewResponseDto(Long id, Integer rating, String comment, ReviewUserInfo user, ReviewCourseInfo course, LocalDateTime createdAt) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReviewUserInfo getUser() {
        return user;
    }

    public void setUser(ReviewUserInfo user) {
        this.user = user;
    }

    public ReviewCourseInfo getCourse() {
        return course;
    }

    public void setCourse(ReviewCourseInfo course) {
        this.course = course;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Nested DTO for simplified User information
    public static class ReviewUserInfo {
        private Long id;
        private String fullName;
        private String email;
        private String avatar;

        public ReviewUserInfo() {
        }

        public ReviewUserInfo(Long id, String fullName, String email, String avatar) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.avatar = avatar;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    // Nested DTO for simplified Course information
    public static class ReviewCourseInfo {
        private Long id;
        private String title;

        public ReviewCourseInfo() {
        }

        public ReviewCourseInfo(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
