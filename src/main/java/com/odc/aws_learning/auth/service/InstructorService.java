package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.app.service.EmailAsyncService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.dao.request.InstructorUpdateRequest;
import com.odc.aws_learning.auth.dto.InstructorWithUserDto;
import com.odc.aws_learning.auth.entities.Instructor;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.InstructorRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository; // To manage User entity
    private final SendEmailService sendEmailService;
    private final EmailAsyncService emailAsyncService;
    private final com.odc.aws_learning.app.service.AuditService auditService;
    private final com.odc.aws_learning.app.service.NotificationService notificationService;
    private final com.odc.aws_learning.auth.repository.AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.frontend.url:https://admin.smart-odc.com}")
    private String frontendUrl;
    
    @Value("${app.dashboard.url:https://admin.smart-odc.com}")
    private String dashboardUrl;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class})
    public CResponse<?> createInstructorAuthenticated(String userEmail, String biography, String specialization) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        // Vérifier si l'utilisateur a déjà un mot de passe, sinon lui attribuer le mot de passe par défaut
        String plainPassword = null;
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            String defaultPassword = "formateur@odl";
            plainPassword = defaultPassword;
            user.setPassword(passwordEncoder.encode(defaultPassword));
            userRepository.save(user);
        }

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        // Créer un log d'activité dans une transaction séparée pour éviter rollback-only
        try {
            logActivityAsync(user.getId(), savedInstructor.getId(), user.getFullName(), user.getEmail());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
        }

        // Retourner le succès AVANT d'envoyer les notifications pour éviter que l'erreur de notification
        // ne marque la transaction comme rollback-only
        CResponse<?> successResponse = CResponse.success(savedInstructor, "Instructeur créé avec succès.");
        
        // Créer une notification pour l'admin dans une transaction séparée
        try {
            sendAdminNotificationsAsync(user);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
        }

        // Envoyer un email de bienvenue au formateur de manière asynchrone
        try {
            String fullName = user.getFullName() != null && !user.getFullName().trim().isEmpty()
                ? user.getFullName()
                : user.getEmail();
            
            // Si un nouveau mot de passe a été attribué, l'envoyer dans l'email
            String emailMessage;
            if (plainPassword != null) {
                emailMessage = sendEmailService.mailTemplateInstructorCreated(
                    fullName,
                    user.getEmail(),
                    plainPassword,
                    dashboardUrl
                );
            } else {
                // Si l'utilisateur avait déjà un mot de passe, envoyer un email sans mot de passe
                emailMessage = sendEmailService.mailTemplateInstructorCreatedWithoutPassword(
                    fullName,
                    user.getEmail(),
                    dashboardUrl
                );
            }
            String subject = "Bienvenue sur Orange Digital Learning - Votre compte formateur a été créé";
            
            System.out.println("=== ENVOI D'EMAIL DE BIENVENUE AU FORMATEUR (ASYNC) ===");
            System.out.println("Email destinataire: " + user.getEmail());
            
            emailAsyncService.sendEmailAsync(user.getEmail(), emailMessage, subject)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.err.println("❌ ÉCHEC DE L'ENVOI DE L'EMAIL AU FORMATEUR");
                        System.err.println("Destinataire: " + user.getEmail());
                        System.err.println("Erreur: " + throwable.getMessage());
                        throwable.printStackTrace(System.err);
                    } else {
                        System.out.println("✅ Email de bienvenue envoyé avec succès au formateur: " + user.getEmail());
                    }
                });
        } catch (Exception e) {
            // Ne pas faire échouer la création si l'email échoue
            System.err.println("❌ Erreur lors de la préparation de l'envoi de l'email au formateur: " + e.getMessage());
            e.printStackTrace();
        }

        return successResponse;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class})
    public CResponse<?> createInstructorForUser(Long userId, String biography, String specialization, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        // Mot de passe par défaut si aucun n'est fourni
        String defaultPassword = "formateur@odl";
        String passwordToUse = (password != null && !password.trim().isEmpty()) ? password : defaultPassword;
        String plainPassword = passwordToUse; // Garder le mot de passe en clair pour l'email
        
        // Crypter et sauvegarder le mot de passe dans le User
        user.setPassword(passwordEncoder.encode(passwordToUse));
        userRepository.save(user);

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        // Créer un log d'activité dans une transaction séparée pour éviter rollback-only
        try {
            logActivityAsync(user.getId(), savedInstructor.getId(), user.getFullName(), user.getEmail());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
        }

        // Retourner le succès AVANT d'envoyer les notifications pour éviter que l'erreur de notification
        // ne marque la transaction comme rollback-only
        CResponse<?> successResponse = CResponse.success(savedInstructor, "Instructeur créé avec succès.");
        
        // Créer une notification pour l'admin dans une transaction séparée
        try {
            sendAdminNotificationsAsync(user);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
        }

        // Envoyer un email de bienvenue au formateur de manière asynchrone
        try {
            String fullName = user.getFullName() != null && !user.getFullName().trim().isEmpty()
                ? user.getFullName()
                : user.getEmail();
            
            // Toujours envoyer le mot de passe dans l'email (en clair pour que le formateur puisse se connecter)
            String emailMessage = sendEmailService.mailTemplateInstructorCreated(
                fullName,
                user.getEmail(),
                plainPassword, // Mot de passe en clair pour l'email
                dashboardUrl // Lien du dashboard
            );
            String subject = "Bienvenue sur Orange Digital Learning - Votre compte formateur a été créé";
            
            System.out.println("=== ENVOI D'EMAIL DE BIENVENUE AU FORMATEUR (ASYNC) ===");
            System.out.println("Email destinataire: " + user.getEmail());
            
            emailAsyncService.sendEmailAsync(user.getEmail(), emailMessage, subject)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.err.println("❌ ÉCHEC DE L'ENVOI DE L'EMAIL AU FORMATEUR");
                        System.err.println("Destinataire: " + user.getEmail());
                        System.err.println("Erreur: " + throwable.getMessage());
                        throwable.printStackTrace(System.err);
                    } else {
                        System.out.println("✅ Email de bienvenue envoyé avec succès au formateur: " + user.getEmail());
                    }
                });
        } catch (Exception e) {
            // Ne pas faire échouer la création si l'email échoue
            System.err.println("❌ Erreur lors de la préparation de l'envoi de l'email au formateur: " + e.getMessage());
            e.printStackTrace();
        }

        return successResponse;
    }

    /**
     * Envoyer des notifications aux admins dans une transaction séparée
     * pour éviter que l'erreur de notification ne fasse échouer la création de l'instructeur
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void sendAdminNotificationsAsync(User user) {
        try {
            // Récupérer tous les admins pour leur envoyer une notification
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
            // Log l'erreur mais ne pas la propager pour ne pas affecter la transaction principale
            System.err.println("Erreur lors de l'envoi asynchrone des notifications aux admins: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Créer un log d'activité dans une transaction séparée
     * pour éviter que l'erreur de log ne fasse échouer la création de l'instructeur
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void logActivityAsync(Long userId, Long instructorId, String fullName, String email) {
        try {
            auditService.logActivity(userId, "create", "instructor", 
                "{\"instructorId\":" + instructorId + ",\"userName\":\"" + 
                (fullName != null ? fullName : email) + "\"}");
        } catch (Exception e) {
            // Log l'erreur mais ne pas la propager pour ne pas affecter la transaction principale
            System.err.println("Erreur lors de l'enregistrement asynchrone du log d'activité: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CResponse<?> getAllInstructors() {
        // Utiliser JOIN FETCH pour charger la relation user de manière eager
        List<Instructor> instructors = instructorRepository.findAllWithUserJoinFetch();
        // Convertir en DTO pour inclure les données User dans la réponse JSON
        List<InstructorWithUserDto> instructorDtos = instructors.stream()
                .map(InstructorWithUserDto::fromInstructor)
                .collect(Collectors.toList());
        return CResponse.success(instructorDtos, "Liste des instructeurs.");
    }

    public CResponse<?> getInstructorById(Long id) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        return instructorOptional.map(instructor -> CResponse.success(instructor, "Instructeur trouvé."))
                .orElse(CResponse.error("Instructeur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> updateInstructor(Long id, InstructorUpdateRequest request) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    // Mettre à jour les champs spécifiques de l'instructeur
                    if (request.getBiography() != null) {
                        instructor.setBiography(request.getBiography());
                    }
                    if (request.getSpecialization() != null) {
                        instructor.setSpecialization(request.getSpecialization());
                    }

                    // Mettre à jour les champs User si fournis
                    User user = instructor.getUser();
                    if (user != null) {
                        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                            user.setFullName(request.getFullName().trim());
                        }
                        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                            user.setEmail(request.getEmail().trim());
                        }
                        if (request.getPhone() != null) {
                            user.setPhone(request.getPhone().trim().isEmpty() ? null : request.getPhone().trim());
                        }
                        if (request.getAvatar() != null) {
                            user.setAvatar(request.getAvatar());
                        }
                        if (request.getActivate() != null) {
                            user.setActivate(request.getActivate());
                        }
                        userRepository.save(user);
                    }

                    return CResponse.success(instructorRepository.save(instructor), "Instructeur mis à jour avec succès.");
                }).orElse(CResponse.error("Instructeur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> deleteInstructor(Long id) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        if (instructorOptional.isPresent()) {
            Instructor instructor = instructorOptional.get();
            User user = instructor.getUser();
            
            // Supprimer l'utilisateur, ce qui supprimera automatiquement l'Instructor en cascade
            // grâce à CascadeType.ALL et orphanRemoval = true dans la relation User -> Instructor
            userRepository.delete(user);
            
            return CResponse.success(null, "Instructeur et utilisateur associé supprimés avec succès.");
        }
        return CResponse.error("Instructeur non trouvé avec l'ID: " + id);
    }
}
