package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.LessonType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LessonProgressDto {
    private Long lessonId;
    private String lessonTitle;
    private LessonType lessonType;
    private Integer lessonDuration;
    private boolean completed;
    private LocalDateTime completedAt;
    /** IDs des quiz associés à cette leçon (pour afficher "Quiz" dans le dash apprenant). */
    private List<Long> quizIds = new ArrayList<>();
    /** IDs des labs associés à cette leçon (pour afficher "Lab" / "TD" dans le dash apprenant). */
    private List<Long> labIds = new ArrayList<>();

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

    public List<Long> getQuizIds() {
        return quizIds;
    }

    public void setQuizIds(List<Long> quizIds) {
        this.quizIds = quizIds != null ? quizIds : new ArrayList<>();
    }

    public List<Long> getLabIds() {
        return labIds;
    }

    public void setLabIds(List<Long> labIds) {
        this.labIds = labIds != null ? labIds : new ArrayList<>();
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
        private List<Long> quizIds = new ArrayList<>();
        private List<Long> labIds = new ArrayList<>();

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

        public LessonProgressDtoBuilder quizIds(List<Long> quizIds) {
            this.quizIds = quizIds != null ? quizIds : new ArrayList<>();
            return this;
        }

        public LessonProgressDtoBuilder labIds(List<Long> labIds) {
            this.labIds = labIds != null ? labIds : new ArrayList<>();
            return this;
        }

        public LessonProgressDto build() {
            LessonProgressDto dto = new LessonProgressDto(lessonId, lessonTitle, lessonType, lessonDuration, completed, completedAt);
            dto.setQuizIds(quizIds);
            dto.setLabIds(labIds);
            return dto;
        }

        public String toString() {
            return "LessonProgressDto.LessonProgressDtoBuilder(lessonId=" + this.lessonId + ", lessonTitle=" + this.lessonTitle + ", lessonType=" + this.lessonType + ", lessonDuration=" + this.lessonDuration + ", completed=" + this.completed + ", completedAt=" + this.completedAt + ")";
        }
    }
}
