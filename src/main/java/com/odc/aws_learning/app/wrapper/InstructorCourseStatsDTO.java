package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InstructorCourseStatsDTO {
    private Long courseId;
    private String courseTitle;
    private long totalEnrollments;
    private double averageCompletionRate;
    private List<LearnerProgressDTO> learnerStats;
}
