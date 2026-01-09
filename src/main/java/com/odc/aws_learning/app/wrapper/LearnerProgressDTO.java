package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearnerProgressDTO {
    private Long learnerId;
    private String learnerName;
    private double completionPercentage;
    private Double bestQuizScore;
}
