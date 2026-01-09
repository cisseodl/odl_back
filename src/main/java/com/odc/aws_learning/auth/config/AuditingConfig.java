package com.odc.aws_learning.auth.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware") // Make it a Spring component
public class AuditingConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("anonymous"); // Or Optional.empty() if you prefer null for unauthenticated actions
        }

        // Get the principal (usually UserDetails, which has the username/email)
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername()); // Username is typically the email
        } else if (principal instanceof String) {
            // For cases where principal is just a String (e.g., anonymousUser)
            return Optional.of((String) principal);
        }

        return Optional.empty(); // Should not happen for authenticated users
    }
}
