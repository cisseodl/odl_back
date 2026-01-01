package com.odc.aws_learning.app.dto;


import java.util.List;

public class CourseProgressDto {
    private Long courseId;
    private String courseTitle;
    private int totalLessons;
    private int completedLessons;
    private double progressPercentage;
    private List<LessonProgressDto> lessons;

    public CourseProgressDto() {
    }

    public CourseProgressDto(Long courseId, String courseTitle, int totalLessons, int completedLessons, double progressPercentage, List<LessonProgressDto> lessons) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
        this.progressPercentage = progressPercentage;
        this.lessons = lessons;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public List<LessonProgressDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonProgressDto> lessons) {
        this.lessons = lessons;
    }

    public static CourseProgressDtoBuilder builder() {
        return new CourseProgressDtoBuilder();
    }

    public static class CourseProgressDtoBuilder {
        private Long courseId;
        private String courseTitle;
        private int totalLessons;
        private int completedLessons;
        private double progressPercentage;
        private List<LessonProgressDto> lessons;

        CourseProgressDtoBuilder() {
        }

        public CourseProgressDtoBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public CourseProgressDtoBuilder courseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
            return this;
        }

        public CourseProgressDtoBuilder totalLessons(int totalLessons) {
            this.totalLessons = totalLessons;
            return this;
        }

        public CourseProgressDtoBuilder completedLessons(int completedLessons) {
            this.completedLessons = completedLessons;
            return this;
        }

        public CourseProgressDtoBuilder progressPercentage(double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }

        public CourseProgressDtoBuilder lessons(List<LessonProgressDto> lessons) {
            this.lessons = lessons;
            return this;
        }

        public CourseProgressDto build() {
            return new CourseProgressDto(courseId, courseTitle, totalLessons, completedLessons, progressPercentage, lessons);
        }

        public String toString() {
            return "CourseProgressDto.CourseProgressDtoBuilder(courseId=" + this.courseId + ", courseTitle=" + this.courseTitle + ", totalLessons=" + this.totalLessons + ", completedLessons=" + this.completedLessons + ", progressPercentage=" + this.progressPercentage + ", lessons=" + this.lessons + ")";
        }
    }
}
