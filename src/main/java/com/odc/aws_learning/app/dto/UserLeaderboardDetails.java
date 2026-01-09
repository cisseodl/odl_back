package com.odc.aws_learning.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLeaderboardDetails {
    private List<CourseInfo> completedCoursesList;
    private List<CertificationInfo> certificationsList;
}
