package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserActivitySummaryDTO {
    private Long userId;
    private String userName;
    private String userEmail;
    private long coursesEnrolled;
    private long coursesCompleted;
    private Double averageQuizScore;
    private LocalDateTime lastSeen;
}
