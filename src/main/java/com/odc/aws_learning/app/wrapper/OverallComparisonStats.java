package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverallComparisonStats {
    private long registrationsCurrentPeriod;
    private long registrationsPreviousPeriod;
    private double completionRateCurrentPeriod;
    private double completionRatePreviousPeriod;
    private long coursesCreatedCurrentPeriod;
    private long coursesCreatedPreviousPeriod;
    private long activeUsersCurrentPeriod;
    private long activeUsersPreviousPeriod;
    private long inactiveUsersCurrentPeriod;
    private long inactiveUsersPreviousPeriod;
}
