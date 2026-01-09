package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoursePerformanceDataPoint {
    private Long courseId;
    private String courseTitle;
    private long enrollments;
    private double completionRate;
    private double averageRating;
    private String period;
}
