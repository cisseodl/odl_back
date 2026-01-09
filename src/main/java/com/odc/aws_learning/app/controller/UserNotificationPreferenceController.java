package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.service.UserNotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users/{userId}/notification-preferences")
@RequiredArgsConstructor
public class UserNotificationPreferenceController {

    private final UserNotificationPreferenceService userNotificationPreferenceService;

    // Helper method to get current user ID (similar to NotificationController)
    private Long getCurrentUserId(Principal principal) {
        // This is a placeholder. In a real application, you would
        // retrieve the actual user ID from the principal/security context.
        // For example, if principal.getName() is the email, you'd fetch the user by email.
        return 1L; // Placeholder: Replace with actual user ID retrieval
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> getUserNotificationPreferences(@PathVariable Long userId, Principal principal) {
        // Ensure the authenticated user is requesting their own preferences
        // Long authenticatedUserId = getCurrentUserId(principal);
        // if (!userId.equals(authenticatedUserId)) {
        //     return CResponse.error("Accès non autorisé aux préférences de cet utilisateur.");
        // }
        return userNotificationPreferenceService.getUserNotificationPreferences(userId);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public CResponse<?> updateUserNotificationPreferences(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean emailNotificationsEnabled,
            @RequestParam(required = false) Boolean smsNotificationsEnabled,
            Principal principal) {
        // Ensure the authenticated user is updating their own preferences
        // Long authenticatedUserId = getCurrentUserId(principal);
        // if (!userId.equals(authenticatedUserId)) {
        //     return CResponse.error("Accès non autorisé pour modifier les préférences de cet utilisateur.");
        // }
        return userNotificationPreferenceService.updateUserNotificationPreferences(userId, emailNotificationsEnabled, smsNotificationsEnabled);
    }
}
