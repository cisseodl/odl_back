package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.ActivityLog;
import com.odc.aws_learning.app.repository.ActivityLogRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public void logActivity(Long userId, String action, String resource, String details) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            ActivityLog log = new ActivityLog(user, user.getFullName(), action, resource, details);
            activityLogRepository.save(log);
        });
    }

    public CResponse<?> getRecentActivity(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ActivityLog> recentActivities = activityLogRepository.findByOrderByCreatedAtDesc(pageable);
        return CResponse.success(recentActivities, "Recent activities retrieved successfully.");
    }

    public CResponse<?> getInstructorRecentActivity(Long instructorId, int limit) {
        // First, check if the user exists and is an instructor (optional, but good practice)
        Optional<User> userOptional = userRepository.findById(instructorId);
        if (userOptional.isEmpty() || userOptional.get().getInstructor() == null) {
            return CResponse.error("Instructor not found or user is not an instructor.");
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<ActivityLog> instructorActivities = activityLogRepository.findByUserIdOrderByCreatedAtDesc(instructorId, pageable);
        return CResponse.success(instructorActivities, "Instructor recent activities retrieved successfully.");
    }
}
