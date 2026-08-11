package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.InstructorAnalyticsService;
import com.odc.aws_learning.app.wrapper.InstructorCourseStatsDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructor/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public class InstructorAnalyticsController {

    private final InstructorAnalyticsService instructorAnalyticsService;

    @GetMapping("/course/{courseId}")
    public CResponse<InstructorCourseStatsDTO> getCourseStats(@PathVariable Long courseId) {
        // TODO: Add security check to ensure the authenticated instructor owns this course
        InstructorCourseStatsDTO stats = instructorAnalyticsService.getCourseStats(courseId);
        return CResponse.success(stats, "Course statistics fetched successfully.");
    }
}
