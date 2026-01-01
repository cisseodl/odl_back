package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.LessonType;

import com.odc.aws_learning.app.constante.LessonType;

public class LessonDto {
    private Long id;
    private String title;
    private LessonType type;
    private String duration; // Formatted as a string e.g., "15 min"
    private Boolean completed;
    private Boolean locked;

    public LessonDto() {
    }

    public LessonDto(Long id, String title, LessonType type, String duration, Boolean completed, Boolean locked) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.duration = duration;
        this.completed = completed;
        this.locked = locked;
    }

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

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public static LessonDtoBuilder builder() {
        return new LessonDtoBuilder();
    }

    public static class LessonDtoBuilder {
        private Long id;
        private String title;
        private LessonType type;
        private String duration;
        private Boolean completed;
        private Boolean locked;

        LessonDtoBuilder() {
        }

        public LessonDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LessonDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public LessonDtoBuilder type(LessonType type) {
            this.type = type;
            return this;
        }

        public LessonDtoBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public LessonDtoBuilder completed(Boolean completed) {
            this.completed = completed;
            return this;
        }

        public LessonDtoBuilder locked(Boolean locked) {
            this.locked = locked;
            return this;
        }

        public LessonDto build() {
            return new LessonDto(id, title, type, duration, completed, locked);
        }

        public String toString() {
            return "LessonDto.LessonDtoBuilder(id=" + this.id + ", title=" + this.title + ", type=" + this.type + ", duration=" + this.duration + ", completed=" + this.completed + ", locked=" + this.locked + ")";
        }
    }
}
