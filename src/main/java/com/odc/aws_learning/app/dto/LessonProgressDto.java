package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.LessonType;

import com.odc.aws_learning.app.constante.LessonType;

import java.time.LocalDateTime;

public class LessonProgressDto {
    private Long lessonId;
    private String lessonTitle;
    private LessonType lessonType;
    private Integer lessonDuration;
    private boolean completed;
    private LocalDateTime completedAt;

    public LessonProgressDto() {
    }

    public LessonProgressDto(Long lessonId, String lessonTitle, LessonType lessonType, Integer lessonDuration, boolean completed, LocalDateTime completedAt) {
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.lessonType = lessonType;
        this.lessonDuration = lessonDuration;
        this.completed = completed;
        this.completedAt = completedAt;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(Integer lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public static LessonProgressDtoBuilder builder() {
        return new LessonProgressDtoBuilder();
    }

    public static class LessonProgressDtoBuilder {
        private Long lessonId;
        private String lessonTitle;
        private LessonType lessonType;
        private Integer lessonDuration;
        private boolean completed;
        private LocalDateTime completedAt;

        LessonProgressDtoBuilder() {
        }

        public LessonProgressDtoBuilder lessonId(Long lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public LessonProgressDtoBuilder lessonTitle(String lessonTitle) {
            this.lessonTitle = lessonTitle;
            return this;
        }

        public LessonProgressDtoBuilder lessonType(LessonType lessonType) {
            this.lessonType = lessonType;
            return this;
        }

        public LessonProgressDtoBuilder lessonDuration(Integer lessonDuration) {
            this.lessonDuration = lessonDuration;
            return this;
        }

        public LessonProgressDtoBuilder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public LessonProgressDtoBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public LessonProgressDto build() {
            return new LessonProgressDto(lessonId, lessonTitle, lessonType, lessonDuration, completed, completedAt);
        }

        public String toString() {
            return "LessonProgressDto.LessonProgressDtoBuilder(lessonId=" + this.lessonId + ", lessonTitle=" + this.lessonTitle + ", lessonType=" + this.lessonType + ", lessonDuration=" + this.lessonDuration + ", completed=" + this.completed + ", completedAt=" + this.completedAt + ")";
        }
    }
}
