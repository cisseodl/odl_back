package com.odc.aws_learning.app.service;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserNotificationPreferenceService {

    private final UserRepository userRepository;

    public CResponse<?> getUserNotificationPreferences(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        User user = userOptional.get();
        Map<String, Boolean> preferences = new HashMap<>();
        preferences.put("emailNotificationsEnabled", user.getEmailNotificationsEnabled());
        preferences.put("smsNotificationsEnabled", user.getSmsNotificationsEnabled());
        return CResponse.success(preferences, "Préférences de notification récupérées avec succès.");
    }

    @Transactional
    public CResponse<?> updateUserNotificationPreferences(Long userId, Boolean emailNotificationsEnabled, Boolean smsNotificationsEnabled) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        User user = userOptional.get();
        if (emailNotificationsEnabled != null) {
            user.setEmailNotificationsEnabled(emailNotificationsEnabled);
        }
        if (smsNotificationsEnabled != null) {
            user.setSmsNotificationsEnabled(smsNotificationsEnabled);
        }
        userRepository.save(user);
        return CResponse.success(null, "Préférences de notification mises à jour avec succès.");
    }
}
