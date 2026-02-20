package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Envoi de la notification d'inscription dans une transaction séparée (REQUIRES_NEW)
 * pour que toute erreur (ex. NotificationService) ne marque pas la transaction d'inscription
 * comme rollback-only et n'entraîne pas "Transaction silently rolled back".
 */
@Service
public class EnrollmentNotificationSender {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentNotificationSender.class);

    private final NotificationService notificationService;

    public EnrollmentNotificationSender(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {Exception.class})
    public void sendEnrollmentNotification(Long instructorId, User learner, Courses course) {
        try {
            log.info("[EnrollmentNotificationSender] Envoi notification inscription pour instructeur ID: {}", instructorId);
            String learnerName = learner.getFullName() != null ? learner.getFullName() : learner.getEmail();
            String courseTitle = course.getTitle() != null ? course.getTitle() : "le cours";
            String message = learnerName + " s'est inscrit à votre cours: " + courseTitle;
            String link = "/instructor/courses/" + course.getId();
            CResponse<?> response = notificationService.createNotification(instructorId, message, "enrollment", link);
            if (response != null && response.isSuccess()) {
                log.info("[EnrollmentNotificationSender] Notification créée avec succès");
            } else {
                log.warn("[EnrollmentNotificationSender] Réponse notification: {}", response != null ? response.getMessage() : "null");
            }
        } catch (Exception e) {
            log.warn("[EnrollmentNotificationSender] Erreur non bloquante: {}", e.getMessage(), e);
            // noRollbackFor = Exception.class : cette transaction ne sera pas marquée rollback-only
            // et l'inscription au cours reste commitée
        }
    }
}
