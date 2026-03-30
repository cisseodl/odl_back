package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorSideEffectsService {

    private final com.odc.aws_learning.app.service.AuditService auditService;
    private final com.odc.aws_learning.app.service.NotificationService notificationService;
    private final com.odc.aws_learning.auth.repository.AdminRepository adminRepository;

    /**
     * Exécuter hors transaction principale pour éviter rollback-only.
     * Important: service séparé => proxy Spring actif (contrairement à une méthode private).
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAdminNotifications(User user) {
        try {
            List<com.odc.aws_learning.auth.entities.Admin> admins = adminRepository.findAll();
            for (com.odc.aws_learning.auth.entities.Admin admin : admins) {
                User adminUser = admin.getUser();
                if (adminUser != null && !adminUser.getId().equals(user.getId())) {
                    notificationService.createNotification(
                        adminUser.getId(),
                        "Nouvel instructeur créé: " + (user.getFullName() != null ? user.getFullName() : user.getEmail()),
                        "registration",
                        "/admin/users/instructeurs"
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des notifications aux admins (non bloquante): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exécuter hors transaction principale pour éviter rollback-only.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActivity(Long userId, Long instructorId, String fullName, String email) {
        try {
            auditService.logActivity(
                userId,
                "create",
                "instructor",
                "{\"instructorId\":" + instructorId + ",\"userName\":\"" + (fullName != null ? fullName : email) + "\"}"
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du log d'activité (non bloquante): " + e.getMessage());
            e.printStackTrace();
        }
    }
}

