package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Notification;
import com.odc.aws_learning.app.repository.NotificationRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository; // To link notifications to users

    @Transactional
    public CResponse<?> createNotification(Long userId, String message, String type, String link) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        Notification notification = new Notification(userOptional.get(), message, type, link);
        // S'assurer que isRead et isArchived sont initialisés (déjà fait dans le constructeur, mais pour être sûr)
        notification.setRead(false);
        notification.setArchived(false);
        Notification savedNotification = notificationRepository.save(notification);
        return CResponse.success(savedNotification, "Notification créée avec succès.");
    }

    public CResponse<?> getAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return CResponse.success(notifications, "Notifications récupérées avec succès.");
    }

    @Transactional
    public CResponse<?> markNotificationAsRead(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isEmpty()) {
            return CResponse.error("Notification non trouvée avec l'ID: " + notificationId);
        }
        Notification notification = notificationOptional.get();
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return CResponse.success(updatedNotification, "Notification marquée comme lue.");
    }

    @Transactional
    public CResponse<?> markAllNotificationsAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
        return CResponse.success(null, "Toutes les notifications ont été marquées comme lues.");
    }

    @Transactional
    public CResponse<?> archiveNotification(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isEmpty()) {
            return CResponse.error("Notification non trouvée avec l'ID: " + notificationId);
        }
        Notification notification = notificationOptional.get();
        notification.setArchived(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return CResponse.success(updatedNotification, "Notification archivée.");
    }

    @Transactional
    public CResponse<?> deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            return CResponse.error("Notification non trouvée avec l'ID: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
        return CResponse.success(null, "Notification supprimée avec succès.");
    }

    public CResponse<?> getNotificationStats(Long userId) {
        long totalNotifications = notificationRepository.countByUserId(userId);
        long unreadNotifications = notificationRepository.countByUserIdAndIsReadFalse(userId);
        // Assuming there's a custom count method for archived or all (total)
        // For simplicity, totalNotifications already represents all.
        // If specific count for archived is needed, add a custom query in repository.

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", totalNotifications);
        stats.put("unread", unreadNotifications);
        // stats.put("archived", notificationRepository.countByUserIdAndIsArchivedTrue(userId)); // If custom query added

        return CResponse.success(stats, "Statistiques de notification récupérées avec succès.");
    }
}
