package com.odc.aws_learning.app.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearnerProgressResponseDTO {
    private String id; // String representation of learner ID
    private String name;
    private String email;
    private Integer coursesEnrolled;
    private Integer coursesCompleted;
    private Double overallProgress; // Percentage from 0 to 100
    private List<CourseProgressDTO> courses;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseProgressDTO {
        private Long courseId;
        private String courseTitle;
        private Double courseOverallProgress; // Percentage
        private Integer chaptersCompleted;
        private Integer totalChapters;
        private String period; // Learning period (e.g., "2025-01")
    }
}
