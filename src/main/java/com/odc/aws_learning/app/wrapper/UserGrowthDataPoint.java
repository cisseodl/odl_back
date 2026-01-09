package com.odc.aws_learning.app.wrapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGrowthDataPoint {
    private String date;
    private long newUsers;
    private long totalUsers;
}
