package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.service.NotificationService;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // Helper method to get current user ID
    private Long getCurrentUserId(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.map(User::getId).orElse(null);
        }
        return null;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Only authenticated users can get their notifications
    public CResponse<?> getAllNotifications(Principal principal) {
        Long userId = getCurrentUserId(principal); // Replace with actual user ID retrieval
        return notificationService.getAllNotifications(userId);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> markNotificationAsRead(@PathVariable Long id) {
        return notificationService.markNotificationAsRead(id);
    }

    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> markAllNotificationsAsRead(Principal principal) {
        Long userId = getCurrentUserId(principal); // Replace with actual user ID retrieval
        return notificationService.markAllNotificationsAsRead(userId);
    }

    @PutMapping("/{id}/archive")
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> archiveNotification(@PathVariable Long id) {
        return notificationService.archiveNotification(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }

    @GetMapping("/stats")
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> getNotificationStats(Principal principal) {
        Long userId = getCurrentUserId(principal); // Replace with actual user ID retrieval
        return notificationService.getNotificationStats(userId);
    }

    // A POST endpoint for creating notifications could be added here,
    // but typically notifications are created by internal system events or admin actions,
    // not directly by a client through a public endpoint like this.
    // Example: When a new course is assigned, a notification is created.
}
