package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.dao.request.InstructorUpdateRequest;
import com.odc.aws_learning.auth.dto.InstructorWithUserDto;
import com.odc.aws_learning.auth.entities.Instructor;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.InstructorRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removed
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository; // To manage User entity
    private final SendEmailService sendEmailService;
    private final com.odc.aws_learning.app.service.AuditService auditService;
    private final com.odc.aws_learning.app.service.NotificationService notificationService;
    private final com.odc.aws_learning.auth.repository.AdminRepository adminRepository;
    // private final PasswordEncoder passwordEncoder; // Removed
    
    @Value("${app.frontend.url:https://admin.smart-odc.com}")
    private String frontendUrl;
    
    @Value("${app.dashboard.url:https://admin.smart-odc.com}")
    private String dashboardUrl;

    @Transactional
    public CResponse<?> createInstructorAuthenticated(String userEmail, String biography, String specialization) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        // Créer un log d'activité
        try {
            auditService.logActivity(user.getId(), "create", "instructor", 
                "{\"instructorId\":" + savedInstructor.getId() + ",\"userName\":\"" + 
                (user.getFullName() != null ? user.getFullName() : user.getEmail()) + "\"}");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
        }

        // Créer une notification pour l'admin
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
            System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
        }

        return CResponse.success(savedInstructor, "Instructeur créé avec succès.");
    }

    @Transactional
    public CResponse<?> createInstructorForUser(Long userId, String biography, String specialization, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        // Créer un log d'activité
        try {
            auditService.logActivity(user.getId(), "create", "instructor", 
                "{\"instructorId\":" + savedInstructor.getId() + ",\"userName\":\"" + 
                (user.getFullName() != null ? user.getFullName() : user.getEmail()) + "\"}");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
        }

        // Créer une notification pour l'admin
        try {
            // Récupérer tous les admins via AdminRepository
            List<com.odc.aws_learning.auth.entities.Admin> admins = 
                com.odc.aws_learning.auth.repository.AdminRepository.class.cast(
                    org.springframework.beans.factory.BeanFactoryUtils.beanOfType(
                        org.springframework.context.ApplicationContext.class.cast(null),
                        com.odc.aws_learning.auth.repository.AdminRepository.class
                    )
                ).findAll();
            // Alternative: injecter AdminRepository via @RequiredArgsConstructor
            // Pour l'instant, on utilise une approche simplifiée
            // Les notifications seront créées via un service dédié ou un listener d'événements
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
        }

        // Envoyer un email au formateur avec le lien Amplify et le mot de passe non crypté (si fourni par l'admin)
        try {
            if (password != null && !password.trim().isEmpty()) {
                // Si un mot de passe est fourni, l'envoyer dans l'email
                String emailMessage = sendEmailService.mailTemplateInstructorCreated(
                    user.getFullName() != null ? user.getFullName() : user.getEmail(),
                    user.getEmail(),
                    password, // Mot de passe non crypté fourni par l'admin
                    dashboardUrl // Lien Amplify du dashboard
                );
                sendEmailService.sendEmailWithAttachment(
                    user.getEmail(),
                    emailMessage,
                    "Votre compte formateur a été créé - Orange Digital Learning"
                );
            }
        } catch (Exception e) {
            // Ne pas faire échouer la création si l'email échoue
            System.err.println("Erreur lors de l'envoi de l'email au formateur: " + e.getMessage());
            e.printStackTrace();
        }

        return CResponse.success(savedInstructor, "Instructeur créé avec succès.");
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
