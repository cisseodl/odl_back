package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.AdminAnalyticsService;
import com.odc.aws_learning.app.wrapper.AnalyticsMetricsDTO;
import com.odc.aws_learning.app.wrapper.DashboardStatsDTO;
import com.odc.aws_learning.app.wrapper.ModerationSummaryData;
import com.odc.aws_learning.app.wrapper.OverallComparisonStats;
import com.odc.aws_learning.app.wrapper.UserGrowthDataPoint; // Added
import com.odc.aws_learning.app.wrapper.CoursePerformanceDataPoint; // Added
import com.odc.aws_learning.app.wrapper.LearnerProgressResponseDTO; // Added
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Added
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Added
import org.springframework.web.bind.annotation.RestController;

import java.util.List; // Added

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/admin-dashboard-analytics")
    public CResponse<DashboardStatsDTO.AdminStats> getAdminDashboardOverview() {
        DashboardStatsDTO.AdminStats stats = adminAnalyticsService.getAdminOverview();
        return CResponse.success(stats, "Admin dashboard overview fetched successfully.");
    }

    @GetMapping("/moderation-summary")
    public CResponse<ModerationSummaryData> getModerationSummary() {
        ModerationSummaryData summary = adminAnalyticsService.getModerationSummary();
        return CResponse.success(summary, "Moderation summary fetched successfully.");
    }

    @GetMapping("/comparison-stats")
    public CResponse<OverallComparisonStats> getComparisonStats() {
        OverallComparisonStats stats = adminAnalyticsService.getComparisonStats();
        return CResponse.success(stats, "Overall comparison statistics fetched successfully.");
    }

    @GetMapping("/user-growth")
    public CResponse<List<UserGrowthDataPoint>> getUserGrowthData(@RequestParam String timeframe) {
        List<UserGrowthDataPoint> data = adminAnalyticsService.getUserGrowthData(timeframe);
        return CResponse.success(data, "User growth data fetched successfully.");
    }

    @GetMapping("/course-performance")
    public CResponse<List<CoursePerformanceDataPoint>> getCoursePerformanceData(
            @RequestParam(defaultValue = "30d") String timeFilter,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<CoursePerformanceDataPoint> data = adminAnalyticsService.getCoursePerformanceData(timeFilter, startDate, endDate);
        return CResponse.success(data, "Course performance data fetched successfully.");
    }

    @GetMapping("/analytics-metrics")
    public CResponse<AnalyticsMetricsDTO> getAnalyticsMetrics() {
        AnalyticsMetricsDTO metrics = adminAnalyticsService.getAnalyticsMetrics();
        return CResponse.success(metrics, "Analytics metrics fetched successfully.");
    }

    @GetMapping("/learner/{learnerId}/progress")
    public CResponse<LearnerProgressResponseDTO> getLearnerProgress(@PathVariable Long learnerId) {
        // learnerId peut être soit l'ID de l'apprenant, soit l'ID de l'utilisateur
        // On essaie d'abord de trouver un apprenant avec cet ID, sinon on assume que c'est un userId
        LearnerProgressResponseDTO progress = adminAnalyticsService.getLearnerProgress(learnerId);
        return CResponse.success(progress, "Learner progress fetched successfully.");
    }
}
