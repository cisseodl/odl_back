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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private static final Logger logger = LoggerFactory.getLogger(InstructorService.class);

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository; // To manage User entity
    private final SendEmailService sendEmailService;
    private final EmailAsyncService emailAsyncService;
    private final InstructorSideEffectsService sideEffectsService;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.frontend.url:https://admin.smart-odc.com}")
    private String frontendUrl;
    
    @Value("${app.dashboard.url:https://admin.smart-odc.com}")
    private String dashboardUrl;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class})
    public CResponse<?> createInstructorAuthenticated(String userEmail, String biography, String specialization) {
        try {
            logger.info("=== DÉBUT CRÉATION INSTRUCTEUR ===");
            logger.info("User Email: {}", userEmail);
            logger.info("Biography: {}", (biography != null ? biography.substring(0, Math.min(50, biography.length())) : "null"));
            logger.info("Specialization: {}", specialization);
            
            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                logger.error("❌ Utilisateur non trouvé avec l'email: {}", userEmail);
                return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
            }
            User user = userOptional.get();
            logger.info("✅ Utilisateur trouvé avec ID: {}", user.getId());

            if (instructorRepository.findByUserId(user.getId()).isPresent()) {
                logger.error("❌ Cet utilisateur est déjà un instructeur");
                return CResponse.error("Cet utilisateur est déjà un instructeur.");
            }

            // Vérifier si l'utilisateur a déjà un mot de passe, sinon lui attribuer le mot de passe par défaut
            String plainPassword = null;
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                logger.info("Mot de passe manquant, attribution du mot de passe par défaut");
                String defaultPassword = "formateur@odl";
                plainPassword = defaultPassword;
                user.setPassword(passwordEncoder.encode(defaultPassword));
                userRepository.save(user);
                userRepository.flush(); // S'assurer que le mot de passe est bien persisté
                logger.info("✅ Mot de passe par défaut sauvegardé");
            } else {
                logger.info("✅ L'utilisateur a déjà un mot de passe");
            }

            logger.info("Création de l'instructeur...");
            Instructor instructor = new Instructor(user);
            instructor.setBiography(biography);
            instructor.setSpecialization(specialization);
            Instructor savedInstructor = instructorRepository.save(instructor);
            instructorRepository.flush(); // S'assurer que l'instructeur est bien persisté
            logger.info("✅ Instructeur sauvegardé avec ID: {}", savedInstructor.getId());
            
            // Link instructor to user for bidirectional consistency
            logger.info("Liaison de l'instructeur à l'utilisateur...");
            user.setInstructor(savedInstructor);
            userRepository.save(user);
            userRepository.flush(); // S'assurer que la liaison est bien persistée
            logger.info("✅ Liaison User-Instructor configurée");

            // Retourner le succès AVANT d'envoyer les notifications et logs pour éviter que l'erreur
            // ne marque la transaction comme rollback-only
            CResponse<?> successResponse = CResponse.success(savedInstructor, "Instructeur créé avec succès.");
            logger.info("✅ Réponse de succès créée");
            logger.info("=== FIN CRÉATION INSTRUCTEUR (SUCCÈS) ===");

            // Créer un log d'activité dans une transaction séparée pour éviter rollback-only
            try {
                logger.info("Envoi du log d'activité de manière asynchrone...");
                sideEffectsService.logActivity(user.getId(), savedInstructor.getId(), user.getFullName(), user.getEmail());
            } catch (Exception e) {
                logger.warn("⚠️ Erreur lors de la création du log d'activité (non bloquante): {}", e.getMessage(), e);
            }
            
            // Créer une notification pour l'admin dans une transaction séparée
            try {
                logger.info("Envoi des notifications aux admins de manière asynchrone...");
                sideEffectsService.sendAdminNotifications(user);
            } catch (Exception e) {
                logger.warn("⚠️ Erreur lors de la création de la notification (non bloquante): {}", e.getMessage(), e);
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
            String subject = "Bienvenue sur Orange Digital Center - Votre compte formateur a été créé";
            
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
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'instructeur: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la création de l'instructeur: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class})
    public CResponse<?> createInstructorForUser(Long userId, String biography, String specialization, String password) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
            }
            User user = userOptional.get();

            if (instructorRepository.findByUserId(user.getId()).isPresent()) {
                return CResponse.error("Cet utilisateur est déjà un instructeur.");
            }

            // Logique métier : seul l'admin crée les instructeurs. Mot de passe par défaut formateur@odl,
            // envoyé par email ; l'instructeur se connecte avec puis le change.
            String defaultPassword = "formateur@odl";
            String passwordToUse = (password != null && !password.trim().isEmpty()) ? password.trim() : defaultPassword;
            String plainPassword = passwordToUse;
            user.setPassword(passwordEncoder.encode(passwordToUse));
            userRepository.save(user);
            userRepository.flush();

            Instructor instructor = new Instructor(user);
            instructor.setBiography(biography);
            instructor.setSpecialization(specialization);
            Instructor savedInstructor = instructorRepository.save(instructor);
            instructorRepository.flush(); // S'assurer que l'instructeur est bien persisté
            
            // Link instructor to user for bidirectional consistency
            user.setInstructor(savedInstructor);
            userRepository.save(user);
            userRepository.flush(); // S'assurer que la liaison est bien persistée

            // Retourner le succès AVANT d'envoyer les notifications et logs pour éviter que l'erreur
            // ne marque la transaction comme rollback-only
            CResponse<?> successResponse = CResponse.success(savedInstructor, "Instructeur créé avec succès.");

            // Créer un log d'activité dans une transaction séparée pour éviter rollback-only
            try {
                sideEffectsService.logActivity(user.getId(), savedInstructor.getId(), user.getFullName(), user.getEmail());
            } catch (Exception e) {
                System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
            }
            
            // Créer une notification pour l'admin dans une transaction séparée
            try {
                sideEffectsService.sendAdminNotifications(user);
            } catch (Exception e) {
                System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
            }

        // Envoyer un email de bienvenue au formateur de manière asynchrone
        try {
            String fullName = user.getFullName() != null && !user.getFullName().trim().isEmpty()
                ? user.getFullName()
                : user.getEmail();
            
            String emailMessage = sendEmailService.mailTemplateInstructorCreated(
                fullName, user.getEmail(), plainPassword, dashboardUrl);
            String subject = "Bienvenue sur Orange Digital Center - Votre compte formateur a été créé";
            
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
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'instructeur: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la création de l'instructeur: " + e.getMessage());
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
