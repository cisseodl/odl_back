package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.ActivityLog;
import com.odc.aws_learning.app.service.AdminAnalyticsService;
import com.odc.aws_learning.app.wrapper.UserActivitySummaryDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // Moved to correct location
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportsController {

    private final AdminAnalyticsService adminAnalyticsService;
    private final com.odc.aws_learning.app.service.AuditService auditService;

    @GetMapping("/user-activity")
    public CResponse<Page<UserActivitySummaryDTO>> getUserActivitySummary(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserActivitySummaryDTO> userActivity = adminAnalyticsService.getUserActivitySummary(page, size);
        return CResponse.success(userActivity, "User activity summary fetched successfully.");
    }

    @GetMapping("/audit/logs")
    public CResponse<Page<ActivityLog>> getAllAuditLogs(
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String resource,
            @RequestParam(required = false) String startDate, // YYYY-MM-DD
            @RequestParam(required = false) String endDate) { // YYYY-MM-DD
        Page<ActivityLog> auditLogs = adminAnalyticsService.getAuditLogs(pageable, userId, action, resource, startDate, endDate);
        return CResponse.success(auditLogs, "Audit logs fetched successfully.");
    }

    @GetMapping("/audit/logs/{userId}")
    public CResponse<Page<ActivityLog>> getUserAuditLogs(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String resource,
            @RequestParam(required = false) String startDate, // YYYY-MM-DD
            @RequestParam(required = false) String endDate) { // YYYY-MM-DD
        Page<ActivityLog> auditLogs = adminAnalyticsService.getAuditLogs(pageable, userId, action, resource, startDate, endDate);
        return CResponse.success(auditLogs, "User audit logs fetched successfully.");
    }

    @GetMapping("/audit/recent")
    public CResponse<?> getRecentActivity(@RequestParam(defaultValue = "10") int limit) {
        return auditService.getRecentActivity(limit);
    }
}
