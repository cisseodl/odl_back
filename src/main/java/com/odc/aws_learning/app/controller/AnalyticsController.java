package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/user-growth")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getUserGrowth(
            @RequestParam(required = false) String timeFilter,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate) {
        return analyticsService.getUserGrowthData(timeFilter, startDate, endDate);
    }

    @GetMapping("/course-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getCoursePerformance(
            @RequestParam(required = false) String timeFilter,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate) {
        return analyticsService.getCoursePerformanceData(timeFilter, startDate, endDate);
    }

    @GetMapping("/comparison-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getComparisonStats(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String timeFilter) {
        return analyticsService.getComparisonStats(period, timeFilter);
    }

    // Endpoint for instructor-specific activity (moved from /api/audit)
    @GetMapping("/instructor-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getInstructorActivity(
            @RequestParam Long instructorId,
            @RequestParam(defaultValue = "10") int limit) {
        return analyticsService.getInstructorRecentActivity(instructorId, limit);
    }

    // Endpoint for moderation summary (moved from /api/moderation)
    @GetMapping("/moderation/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getModerationSummary() {
        return analyticsService.getModerationSummary();
    }

    // Endpoint for instructor dashboard summary (can be handled by DashboardController or here)
    // Decided to put it here for consistency with other analytics endpoints for instructors.
    @GetMapping("/instructor-dashboard-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getInstructorDashboardPerformance(
            @RequestParam Long instructorId) {
        return analyticsService.getCoursePerformanceForInstructor(instructorId);
    }
}
