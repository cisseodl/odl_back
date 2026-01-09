package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModerationSummaryData {
    private long pendingCourses;
    private long pendingInstructorProfiles;
    private long pendingReviews;
    private long flaggedContent; // Placeholder
    private long totalPending;
}
