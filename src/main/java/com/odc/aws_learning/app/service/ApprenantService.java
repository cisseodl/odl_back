package com.odc.aws_learning.app.service;

import com.odc.aws_learning.auth.dao.request.ApprenantCreateRequest;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.app.repository.CohorteRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.repository.LearnerModuleRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.app.repository.LabSessionRepository;
import com.odc.aws_learning.app.repository.CertificateRepository;
import com.odc.aws_learning.app.repository.EvaluationAttemptRepository;
import com.odc.aws_learning.app.repository.CourseEnrollmentExpectationsRepository;
import com.odc.aws_learning.app.repository.CourseSatisfactionRepository;
import com.odc.aws_learning.app.repository.NotificationRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.app.wrapper.ApprenantWithUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.app.service.EmailAsyncService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprenantService {

    private final ApprenantRepository apprenantRepository;
    private final CohorteRepository cohorteRepository;
    private final UserRepository userRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final LearnerModuleRepository learnerModuleRepository;
    private final UserProgressRepository userProgressRepository;
    private final ReviewRepository reviewRepository;
    private final LabSessionRepository labSessionRepository;
    private final CertificateRepository certificateRepository;
    private final EvaluationAttemptRepository evaluationAttemptRepository;
    private final CourseEnrollmentExpectationsRepository courseEnrollmentExpectationsRepository;
    private final CourseSatisfactionRepository courseSatisfactionRepository;
    private final NotificationRepository notificationRepository;
    private final com.odc.aws_learning.app.repository.CourseFeedbackRepository courseFeedbackRepository;
    private final com.odc.aws_learning.app.repository.ActivityLogRepository activityLogRepository;
    private final com.odc.aws_learning.app.repository.TestimonialRepository testimonialRepository;
    private final SendEmailService sendEmailService;
    private final EmailAsyncService emailAsyncService;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.frontend.apprenant.url:https://smart-odc.com}")
    private String frontendUrl;

    @Transactional
    public CResponse<?> createApprenantAuthenticated(String emailFromJwt, ApprenantCreateRequest request) {
        User user;
        boolean isCreatedByAdmin = false; // Indique si l'apprenant est créé par un admin
        
        // Si userId est fourni, utiliser cet ID (pour permettre aux admins de créer un apprenant pour un autre utilisateur)
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + request.getUserId()));
            isCreatedByAdmin = true; // Création par admin via userId
        } else if (request.getUserEmail() != null) {
            // Si userEmail est fourni, utiliser cet email
            user = userRepository.findByEmail(request.getUserEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email: " + request.getUserEmail()));
            isCreatedByAdmin = true; // Création par admin via userEmail
        } else {
            // Sinon, utiliser l'email du JWT (comportement par défaut - auto-inscription)
            user = userRepository.findByEmail(emailFromJwt)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            isCreatedByAdmin = false; // Auto-inscription par l'apprenant lui-même
        }

        if (apprenantRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un apprenant.");
        }

        // Quand l'admin crée un apprenant : toujours attribuer le mdp par défaut apprenant@odl et l'envoyer par email
        String plainPassword = null;
        if (isCreatedByAdmin) {
            String defaultPassword = "apprenant@odl";
            plainPassword = defaultPassword;
            user.setPassword(passwordEncoder.encode(defaultPassword));
            userRepository.save(user);
            userRepository.flush();
            System.out.println("✅ Mot de passe par défaut 'apprenant@odl' attribué et sauvegardé pour l'apprenant créé par admin");
        }

        Apprenant apprenant = new Apprenant();
        apprenant.setActivate(request.getActivate() != null ? request.getActivate() : true);
        
        // Utiliser le username pour remplir nom et prenom dans Apprenant (pas de modification du User)
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            String[] nameParts = request.getUsername().trim().split("\\s+", 2);
            if (nameParts.length >= 2) {
                apprenant.setPrenom(nameParts[0]);
                apprenant.setNom(nameParts[1]);
            } else {
                apprenant.setPrenom(nameParts[0]);
                apprenant.setNom("");
            }
        }
        // L'email de l'Apprenant vient du User (pas de modification du User)
        apprenant.setEmail(user.getEmail());
        apprenant.setNumero(request.getNumero()); // Le numéro vient du DTO
        apprenant.setProfession(request.getProfession());
        apprenant.setNiveauEtude(request.getNiveauEtude());
        apprenant.setFiliere(request.getFiliere());
        apprenant.setAttentes(request.getAttentes());
        // Utiliser conditionsAccepted si fourni, sinon utiliser satisfaction (compatibilité), sinon true par défaut
        Boolean conditionsAcceptedValue = request.getConditionsAccepted();
        if (conditionsAcceptedValue == null) {
            // Compatibilité avec l'ancien nom (deprecated)
            @SuppressWarnings("deprecation")
            Boolean oldSatisfaction = request.getSatisfaction();
            conditionsAcceptedValue = oldSatisfaction;
        }
        if (conditionsAcceptedValue == null) {
            conditionsAcceptedValue = true; // Valeur par défaut
        }
        apprenant.setConditionsAccepted(conditionsAcceptedValue);
        apprenant.setUser(user);

        // cohorte si présente
        if (request.getCohorteId() != null) {
            Cohorte cohorte = cohorteRepository.findById(request.getCohorteId())
                    .orElseThrow(() -> new RuntimeException("Cohorte introuvable"));
            apprenant.setCohorte(cohorte);
        }

        Apprenant savedApprenant = apprenantRepository.save(apprenant);
        user.setApprenant(savedApprenant);
        userRepository.save(user);

        // Envoyer un email de bienvenue à l'apprenant après création de compte (auto-inscription ou création par admin)
        // IMPORTANT: L'email est envoyé de manière asynchrone pour ne pas bloquer la création du compte
        // Si l'email échoue, on log l'erreur mais on ne fait pas échouer la création
        sendWelcomeEmailAsync(user, savedApprenant, frontendUrl, plainPassword);

        return CResponse.success(savedApprenant, "Apprenant créé avec succès.");
    }

    /**
     * Envoie un email de bienvenue de manière asynchrone avec retry
     * Cette méthode est appelée après la création réussie d'un apprenant
     * @param plainPassword Le mot de passe en clair si l'apprenant a été créé par un admin (null si auto-inscription)
     */
    private void sendWelcomeEmailAsync(User user, Apprenant apprenant, String frontendUrl, String plainPassword) {
        try {
            // Vérifier que l'email est valide
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                System.err.println("❌ ERREUR: Email invalide ou vide: " + user.getEmail());
                return;
            }
            
            // Construire le nom complet
            String fullName = user.getFullName() != null && !user.getFullName().trim().isEmpty() 
                ? user.getFullName() 
                : (apprenant.getPrenom() != null && apprenant.getNom() != null 
                    ? (apprenant.getPrenom() + " " + apprenant.getNom()).trim()
                    : user.getEmail());
            
            System.out.println("=== ENVOI D'EMAIL DE BIENVENUE (ASYNC) ===");
            System.out.println("Email destinataire: " + user.getEmail());
            System.out.println("Nom complet: " + fullName);
            System.out.println("Frontend URL: " + frontendUrl);
            System.out.println("Mot de passe fourni: " + (plainPassword != null ? "Oui" : "Non"));
            
            // Générer le template d'email
            String emailMessage;
            if (plainPassword != null) {
                // Si un mot de passe a été attribué (création par admin), utiliser le template avec mot de passe
                emailMessage = sendEmailService.mailTemplateApprenantCreatedWithPassword(
                    fullName,
                    user.getEmail(),
                    plainPassword,
                    frontendUrl
                );
            } else {
                // Sinon, utiliser le template standard (auto-inscription)
                emailMessage = sendEmailService.mailTemplateApprenantCreated(
                    fullName,
                    user.getEmail(),
                    frontendUrl
                );
            }
            
            String subject = "Bienvenue sur Orange Digital Learning - Votre compte apprenant a été créé";
            
            // Envoyer l'email de manière asynchrone avec retry automatique
            emailAsyncService.sendEmailAsync(
                user.getEmail(),
                emailMessage,
                subject
            ).whenComplete((result, throwable) -> {
                if (throwable != null) {
                    System.err.println("❌ ÉCHEC DE L'ENVOI DE L'EMAIL DE BIENVENUE À L'APPRENANT");
                    System.err.println("Destinataire: " + user.getEmail());
                    System.err.println("Erreur: " + throwable.getMessage());
                    throwable.printStackTrace(System.err);
                } else {
                    System.out.println("✅ Email de bienvenue envoyé avec succès à l'apprenant: " + user.getEmail());
                }
            });
            
        } catch (Exception e) {
            System.err.println("❌ ERREUR LORS DE LA PRÉPARATION DE L'ENVOI D'EMAIL");
            System.err.println("Destinataire: " + user.getEmail());
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    @Transactional(readOnly = true)
    public CResponse<?> getAllApprenants() {
        try {
            // Essayer d'abord avec la méthode optimisée
            List<Apprenant> apprenants;
            try {
                apprenants = apprenantRepository.findAllWithUserAndCohorteJoinFetch();
            } catch (Exception e) {
                // Si la méthode optimisée échoue, utiliser findAll() standard
                System.err.println("Erreur avec findAllWithUserAndCohorteJoinFetch, utilisation de findAll(): " + e.getMessage());
                apprenants = apprenantRepository.findAll();
            }
            
            // Convertir en DTO pour éviter les problèmes de sérialisation JSON
            // Exclure les formateurs (instructeurs) : ne pas les afficher dans Utilisateurs -> Apprenants
            List<ApprenantWithUserDto> apprenantDtos = apprenants.stream()
                    .filter(apprenant -> {
                        if (apprenant.getUser() == null) return true;
                        return apprenant.getUser().getInstructor() == null;
                    })
                    .map(apprenant -> {
                        try {
                            return ApprenantWithUserDto.fromApprenant(apprenant);
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la conversion d'un apprenant en DTO: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            // Inclure les utilisateurs sans fiche Apprenant (ex. inscrits via smart-odc.com) pour qu'ils apparaissent dans Utilisateurs -> Apprenants
            try {
                List<User> allUsersWithRelations = userRepository.findAllWithRelations();
                List<ApprenantWithUserDto> fromUsers = allUsersWithRelations.stream()
                        .filter(u -> u.getAdmin() == null && u.getInstructor() == null && u.getApprenant() == null)
                        .map(ApprenantWithUserDto::fromUser)
                        .filter(dto -> dto != null)
                        .collect(Collectors.toList());
                apprenantDtos.addAll(fromUsers);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'ajout des utilisateurs sans fiche Apprenant: " + e.getMessage());
            }
            
            return CResponse.success(apprenantDtos, "Liste des apprenants.");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des apprenants: " + e.getMessage());
        }
    }

    /**
     * Récupère les apprenants inscrits aux cours de l'instructeur connecté.
     * Réservé aux rôles INSTRUCTOR et ADMIN (ADMIN reçoit tous les apprenants pour compatibilité).
     */
    @Transactional(readOnly = true, noRollbackFor = {Exception.class})
    public CResponse<?> getApprenantsByInstructorCourses(String currentUserEmail) {
        try {
            // Charger admin + instructor pour reconnaître correctement un ADMIN (sinon getAdmin() est lazy et null)
            Optional<User> userOpt = userRepository.findByEmailWithRoles(currentUserEmail);
            if (userOpt.isEmpty()) {
                return CResponse.error("Utilisateur non trouvé.");
            }
            User user = userOpt.get();
            // ADMIN peut appeler cet endpoint : on retourne tous les apprenants (comme get-all)
            if (user.getAdmin() != null && user.getInstructor() == null) {
                return getAllApprenants();
            }
            if (user.getInstructor() == null) {
                return CResponse.error("Accès refusé : réservé aux instructeurs.");
            }
            // courses.instructor_id référence users.id (Courses.instructor = User), pas instructors.id
            Long instructorUserId = user.getId();
            System.out.println("[ApprenantService] Recherche des apprenants pour l'instructeur (user id): " + instructorUserId);
            
            List<Long> learnerIds = null;
            try {
                learnerIds = detailsCourseRepo.findDistinctLearnerIdsByInstructorIdDirect(instructorUserId);
                System.out.println("[ApprenantService] Nombre de learnerIds trouvés (requête native directe): " + (learnerIds != null ? learnerIds.size() : 0));
            } catch (Exception e1) {
                System.out.println("[ApprenantService] Erreur avec requête native directe: " + e1.getMessage());
                try {
                    learnerIds = detailsCourseRepo.findDistinctLearnerIdsByInstructorIdNative(instructorUserId);
                    System.out.println("[ApprenantService] Nombre de learnerIds trouvés (requête native JOIN): " + (learnerIds != null ? learnerIds.size() : 0));
                } catch (Exception e2) {
                    System.out.println("[ApprenantService] Erreur avec requête native JOIN: " + e2.getMessage());
                    try {
                        learnerIds = detailsCourseRepo.findDistinctLearnerIdsByInstructorId(instructorUserId);
                        System.out.println("[ApprenantService] Nombre de learnerIds trouvés (requête JPQL): " + (learnerIds != null ? learnerIds.size() : 0));
                    } catch (Exception e3) {
                        System.out.println("[ApprenantService] Erreur avec requête JPQL: " + e3.getMessage());
                        e3.printStackTrace();
                    }
                }
            }
            
            if (learnerIds != null && !learnerIds.isEmpty()) {
                System.out.println("[ApprenantService] IDs des apprenants: " + learnerIds);
            }
            if (learnerIds == null || learnerIds.isEmpty()) {
                System.out.println("[ApprenantService] Aucun apprenant trouvé pour l'instructeur (user id): " + instructorUserId);
                return CResponse.success(Collections.emptyList(), "Aucun apprenant inscrit à vos cours.");
            }
            List<Apprenant> apprenants = apprenantRepository.findByUser_IdInWithUserAndCohorte(learnerIds);
            System.out.println("[ApprenantService] Nombre d'apprenants récupérés depuis le repository: " + (apprenants != null ? apprenants.size() : 0));
            List<ApprenantWithUserDto> dtos = apprenants.stream()
                    .map(a -> {
                        try {
                            return ApprenantWithUserDto.fromApprenant(a);
                        } catch (Exception e) {
                            System.err.println("[ApprenantService] Erreur lors de la conversion d'un apprenant en DTO: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return CResponse.success(dtos, "Apprenants inscrits à vos cours.");
        } catch (Exception e) {
            System.err.println("[ApprenantService] Erreur générale lors de la récupération des apprenants: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des apprenants: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public CResponse<?> getApprenantById(Long id) {
        try {
            Optional<Apprenant> apprenantOptional = apprenantRepository.findByIdWithUserAndCohorte(id);
            if (apprenantOptional.isPresent()) {
                Apprenant apprenant = apprenantOptional.get();
                // Convertir en DTO pour éviter les problèmes de sérialisation JSON
                ApprenantWithUserDto apprenantDto = ApprenantWithUserDto.fromApprenant(apprenant);
                return CResponse.success(apprenantDto, "Apprenant trouvé.");
            }
            return CResponse.error("Apprenant non trouvé avec l'ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération de l'apprenant: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public CResponse<?> getApprenantStats(Long id) {
        try {
            Optional<Apprenant> apprenantOptional = apprenantRepository.findByIdWithUserAndCohorte(id);
            if (apprenantOptional.isEmpty()) {
                return CResponse.error("Apprenant non trouvé avec l'ID: " + id);
            }
            
            Apprenant apprenant = apprenantOptional.get();
            User user = apprenant.getUser();
            
            if (user == null) {
                return CResponse.error("L'apprenant n'a pas d'utilisateur associé.");
            }
            
            Long userId = user.getId();
            
            // Calculer les statistiques
            long coursesEnrolled = detailsCourseRepo.countByLearnerId(userId);
            long coursesCompleted = detailsCourseRepo.findByLearnerId(userId).stream()
                    .filter(detailsCourse -> detailsCourse.isCompleted())
                    .count();
            long totalCertificates = certificateRepository.countByUser(user);
            
            // Créer un objet de réponse avec les statistiques
            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("coursesEnrolled", coursesEnrolled);
            stats.put("completedCourses", coursesCompleted);
            stats.put("totalCertificates", totalCertificates);
            
            return CResponse.success(stats, "Statistiques de l'apprenant récupérées avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

    @Transactional
    public CResponse<?> updateApprenant(Long id, User userDetails, String username, String numero, String profession, String niveauEtude, String filiere, Long cohorteId, String attentes, Boolean conditionsAccepted) {
        return apprenantRepository.findById(id)
                .map(apprenant -> {
                    // Update user details if necessary (e.g., from a DTO)
                    User user = apprenant.getUser();
                    if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
                    if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
                    // ... update other user fields as needed ...
                    userRepository.save(user); // Save updated user details
                    
                    // Utiliser le username pour remplir nom et prenom dans Apprenant (pas de modification du User)
                    if (username != null && !username.trim().isEmpty()) {
                        String[] nameParts = username.trim().split("\\s+", 2);
                        if (nameParts.length >= 2) {
                            apprenant.setPrenom(nameParts[0]);
                            apprenant.setNom(nameParts[1]);
                        } else {
                            apprenant.setPrenom(nameParts[0]);
                            apprenant.setNom("");
                        }
                    }
                    
                    // Mettre à jour l'email de l'apprenant avec celui du User (pas de modification du User)
                    apprenant.setEmail(user.getEmail());
                    
                    if (numero != null) apprenant.setNumero(numero);
                    if (profession != null) apprenant.setProfession(profession);
                    if (niveauEtude != null) apprenant.setNiveauEtude(niveauEtude);
                    if (filiere != null) apprenant.setFiliere(filiere);
                    if (attentes != null) apprenant.setAttentes(attentes);
                    if (conditionsAccepted != null) apprenant.setConditionsAccepted(conditionsAccepted);

                    if (cohorteId != null) {
                        cohorteRepository.findById(cohorteId).ifPresent(apprenant::setCohorte);
                    } else {
                        apprenant.setCohorte(null); // Explicitly unlink if cohorteId is null
                    }

                    return CResponse.success(apprenantRepository.save(apprenant), "Apprenant mis à jour avec succès.");
                }).orElse(CResponse.error("Apprenant non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> deleteApprenant(Long id) {
        try {
            System.out.println("=== SUPPRESSION D'APPRENANT ===");
            System.out.println("ID Apprenant: " + id);
            
            Optional<Apprenant> apprenantOptional = apprenantRepository.findById(id);
            if (apprenantOptional.isEmpty()) {
                System.err.println("Apprenant non trouvé avec l'ID: " + id);
                return CResponse.error("Apprenant non trouvé avec l'ID: " + id);
            }
            
            Apprenant apprenant = apprenantOptional.get();
            User user = apprenant.getUser();
            
            if (user == null) {
                System.out.println("Aucun utilisateur associé, suppression directe de l'apprenant");
                apprenantRepository.delete(apprenant);
                return CResponse.success(null, "Apprenant supprimé avec succès.");
            }
            
            System.out.println("User ID: " + user.getId());
            
            // IMPORTANT: Ordre de suppression pour éviter les contraintes de clé étrangère
            // 1. CourseEnrollmentExpectations (référence DetailsCourse) - DOIT être supprimé AVANT DetailsCourse
            // 2. CourseFeedback (référence DetailsCourse) - DOIT être supprimé AVANT DetailsCourse
            // 3. DetailsCourse
            // 4. CourseSatisfaction (référence EvaluationAttempt) - DOIT être supprimé AVANT EvaluationAttempt
            // 5. EvaluationAttempt
            // 6. Autres relations...
            
            // 1. Supprimer CourseEnrollmentExpectations AVANT DetailsCourse
            try {
                System.out.println("Suppression des CourseEnrollmentExpectations...");
                List<com.odc.aws_learning.app.entity.DetailsCourse> detailsCoursesForExpectations = detailsCourseRepo.findByLearnerId(user.getId());
                final int[] expectationsDeleted = {0};
                for (com.odc.aws_learning.app.entity.DetailsCourse dc : detailsCoursesForExpectations) {
                    courseEnrollmentExpectationsRepository.findByDetailsCourseId(dc.getId())
                            .ifPresent(expectation -> {
                                courseEnrollmentExpectationsRepository.delete(expectation);
                                expectationsDeleted[0]++;
                            });
                }
                System.out.println("CourseEnrollmentExpectations supprimés: " + expectationsDeleted[0]);
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des CourseEnrollmentExpectations pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des CourseEnrollmentExpectations: " + e.getMessage(), e);
            }
            
            // 2. Supprimer CourseFeedback AVANT DetailsCourse
            try {
                System.out.println("Suppression des CourseFeedback...");
                List<com.odc.aws_learning.app.entity.DetailsCourse> detailsCoursesForFeedback = detailsCourseRepo.findByLearnerId(user.getId());
                final int[] feedbackDeleted = {0};
                for (com.odc.aws_learning.app.entity.DetailsCourse dc : detailsCoursesForFeedback) {
                    courseFeedbackRepository.findByDetailsCourseId(dc.getId())
                            .ifPresent(feedback -> {
                                courseFeedbackRepository.delete(feedback);
                                feedbackDeleted[0]++;
                            });
                }
                System.out.println("CourseFeedback supprimés: " + feedbackDeleted[0]);
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des CourseFeedback pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des CourseFeedback: " + e.getMessage(), e);
            }
            
            // 3. Supprimer DetailsCourse (inscriptions aux cours) associés à cet utilisateur
            // car DetailsCourse n'a pas de cascade depuis User
            try {
                System.out.println("Suppression des DetailsCourse...");
                List<com.odc.aws_learning.app.entity.DetailsCourse> detailsCourses = detailsCourseRepo.findByLearnerId(user.getId());
                System.out.println("Nombre de DetailsCourse trouvés: " + detailsCourses.size());
                if (!detailsCourses.isEmpty()) {
                    detailsCourseRepo.deleteAll(detailsCourses);
                    System.out.println("DetailsCourse supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des DetailsCourse pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des DetailsCourse: " + e.getMessage(), e);
            }
            
            // 5. Supprimer les UserQuizAttempt car cette relation n'a pas de cascade depuis User
            try {
                System.out.println("Suppression des UserQuizAttempt...");
                List<com.odc.aws_learning.app.entity.UserQuizAttempt> quizAttempts = userQuizAttemptRepository.findByUserId(user.getId());
                System.out.println("Nombre de UserQuizAttempt trouvés: " + quizAttempts.size());
                if (!quizAttempts.isEmpty()) {
                    userQuizAttemptRepository.deleteAll(quizAttempts);
                    System.out.println("UserQuizAttempt supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des UserQuizAttempt pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des UserQuizAttempt: " + e.getMessage(), e);
            }
            
            // Supprimer les LearnerModule car cette relation utilise "learner" au lieu de "user"
            try {
                System.out.println("Suppression des LearnerModule...");
                List<com.odc.aws_learning.app.entity.LearnerModule> learnerModules = learnerModuleRepository.findAll().stream()
                        .filter(lm -> lm.getLearner() != null && lm.getLearner().getId().equals(user.getId()))
                        .collect(java.util.stream.Collectors.toList());
                System.out.println("Nombre de LearnerModule trouvés: " + learnerModules.size());
                if (!learnerModules.isEmpty()) {
                    learnerModuleRepository.deleteAll(learnerModules);
                    System.out.println("LearnerModule supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des LearnerModule pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des LearnerModule: " + e.getMessage(), e);
            }
            
            // Supprimer les UserProgress car cette relation peut avoir des problèmes de cascade
            try {
                System.out.println("Suppression des UserProgress...");
                List<com.odc.aws_learning.app.entity.UserProgress> userProgresses = userProgressRepository.findByUserId(user.getId());
                System.out.println("Nombre de UserProgress trouvés: " + userProgresses.size());
                if (!userProgresses.isEmpty()) {
                    userProgressRepository.deleteAll(userProgresses);
                    System.out.println("UserProgress supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des UserProgress pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des UserProgress: " + e.getMessage(), e);
            }
            
            // Supprimer les Review car cette relation peut avoir des problèmes de cascade
            try {
                System.out.println("Suppression des Review...");
                List<com.odc.aws_learning.app.entity.Review> reviews = reviewRepository.findByUserId(user.getId());
                System.out.println("Nombre de Review trouvés: " + reviews.size());
                if (!reviews.isEmpty()) {
                    reviewRepository.deleteAll(reviews);
                    System.out.println("Review supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des Review pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des Review: " + e.getMessage(), e);
            }
            
            // Supprimer les LabSession car cette relation peut avoir des problèmes de cascade
            try {
                System.out.println("Suppression des LabSession...");
                List<com.odc.aws_learning.app.entity.LabSession> labSessions = labSessionRepository.findByUserId(user.getId());
                System.out.println("Nombre de LabSession trouvés: " + labSessions.size());
                if (!labSessions.isEmpty()) {
                    labSessionRepository.deleteAll(labSessions);
                    System.out.println("LabSession supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des LabSession pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des LabSession: " + e.getMessage(), e);
            }
            
            // 3. IMPORTANT: Supprimer CourseSatisfaction AVANT EvaluationAttempt
            // car CourseSatisfaction a une clé étrangère vers EvaluationAttempt
            try {
                System.out.println("Suppression des CourseSatisfaction...");
                List<com.odc.aws_learning.app.entity.CourseSatisfaction> satisfactions = courseSatisfactionRepository.findAll().stream()
                        .filter(cs -> cs.getUser() != null && cs.getUser().getId().equals(user.getId()))
                        .collect(java.util.stream.Collectors.toList());
                System.out.println("Nombre de CourseSatisfaction trouvés: " + satisfactions.size());
                if (!satisfactions.isEmpty()) {
                    courseSatisfactionRepository.deleteAll(satisfactions);
                    System.out.println("CourseSatisfaction supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des CourseSatisfaction pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des CourseSatisfaction: " + e.getMessage(), e);
            }
            
            // 4. Supprimer les EvaluationAttempt APRÈS CourseSatisfaction
            try {
                System.out.println("Suppression des EvaluationAttempt...");
                List<com.odc.aws_learning.app.entity.EvaluationAttempt> evaluationAttempts = evaluationAttemptRepository.findByUser(user);
                System.out.println("Nombre de EvaluationAttempt trouvés: " + evaluationAttempts.size());
                if (!evaluationAttempts.isEmpty()) {
                    evaluationAttemptRepository.deleteAll(evaluationAttempts);
                    System.out.println("EvaluationAttempt supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des EvaluationAttempt pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des EvaluationAttempt: " + e.getMessage(), e);
            }
            
            // Supprimer les Certificate car cette relation peut avoir des problèmes de cascade
            try {
                System.out.println("Suppression des Certificate...");
                // Utiliser une requête pour trouver les certificats par utilisateur
                List<com.odc.aws_learning.app.entity.Certificate> certificates = certificateRepository.findAll().stream()
                        .filter(cert -> cert.getUser() != null && cert.getUser().getId().equals(user.getId()))
                        .collect(java.util.stream.Collectors.toList());
                System.out.println("Nombre de Certificate trouvés: " + certificates.size());
                if (!certificates.isEmpty()) {
                    certificateRepository.deleteAll(certificates);
                    System.out.println("Certificate supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des Certificate pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des Certificate: " + e.getMessage(), e);
            }
            
            // Supprimer les Notification
            try {
                System.out.println("Suppression des Notification...");
                List<com.odc.aws_learning.app.entity.Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
                System.out.println("Nombre de Notification trouvés: " + notifications.size());
                if (!notifications.isEmpty()) {
                    notificationRepository.deleteAll(notifications);
                    System.out.println("Notification supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des Notification pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des Notification: " + e.getMessage(), e);
            }
            
            // Supprimer les ActivityLog
            try {
                System.out.println("Suppression des ActivityLog...");
                // La méthode findByUserIdOrderByCreatedAtDesc retourne une List (avec Pageable pour la pagination)
                org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE);
                List<com.odc.aws_learning.app.entity.ActivityLog> activityLogs = activityLogRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
                System.out.println("Nombre de ActivityLog trouvés: " + activityLogs.size());
                if (!activityLogs.isEmpty()) {
                    activityLogRepository.deleteAll(activityLogs);
                    System.out.println("ActivityLog supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des ActivityLog pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des ActivityLog: " + e.getMessage(), e);
            }
            
            // Supprimer les Testimonial
            try {
                System.out.println("Suppression des Testimonial...");
                List<com.odc.aws_learning.app.entity.Testimonial> testimonials = testimonialRepository.findByUserId(user.getId());
                System.out.println("Nombre de Testimonial trouvés: " + testimonials.size());
                if (!testimonials.isEmpty()) {
                    testimonialRepository.deleteAll(testimonials);
                    System.out.println("Testimonial supprimés avec succès");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des Testimonial pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la suppression des Testimonial: " + e.getMessage(), e);
            }
            
            // Supprimer d'abord l'Apprenant avant l'utilisateur pour éviter les problèmes de contrainte
            System.out.println("Suppression de l'apprenant...");
            apprenantRepository.delete(apprenant);
            System.out.println("Apprenant supprimé avec succès");
            
            System.out.println("Suppression de l'utilisateur...");
            // Supprimer l'utilisateur après avoir supprimé toutes les relations
            userRepository.delete(user);
            System.out.println("Utilisateur supprimé avec succès");
            
            return CResponse.success(null, "Apprenant et utilisateur associé supprimés avec succès.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            System.err.println("=== DataIntegrityViolationException ===");
            System.err.println("Message: " + errorMessage);
            System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            if (e.getCause() != null) {
                System.err.println("=== Stack Trace de la Cause ===");
                e.getCause().printStackTrace();
            }
            // Logger la stack trace complète
            System.err.println("=== Stack Trace Complète ===");
            e.printStackTrace();
            
            if (errorMessage != null && (errorMessage.contains("foreign key constraint") || errorMessage.contains("constraint"))) {
                // Extraire le nom de la contrainte si possible
                String constraintName = "";
                if (errorMessage.contains("constraint") && errorMessage.contains("\"")) {
                    int start = errorMessage.indexOf("\"");
                    int end = errorMessage.indexOf("\"", start + 1);
                    if (end > start) {
                        constraintName = errorMessage.substring(start + 1, end);
                    }
                }
                return CResponse.error("Impossible de supprimer l'apprenant car il est référencé par d'autres données. " + 
                    (constraintName.isEmpty() ? "" : "Contrainte: " + constraintName + ". ") +
                    "Veuillez d'abord supprimer les données associées. Détails: " + errorMessage);
            }
            return CResponse.error("Erreur de contrainte lors de la suppression de l'apprenant: " + errorMessage);
        } catch (RuntimeException e) {
            // Si c'est une RuntimeException qu'on a lancée nous-mêmes, la propager avec plus de détails
            e.printStackTrace();
            String errorMessage = e.getMessage();
            System.err.println("=== RuntimeException ===");
            System.err.println("Message: " + errorMessage);
            System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            if (e.getCause() != null) {
                System.err.println("=== Stack Trace de la Cause ===");
                e.getCause().printStackTrace();
            }
            System.err.println("=== Stack Trace Complète ===");
            e.printStackTrace();
            return CResponse.error("Erreur lors de la suppression de l'apprenant: " + (errorMessage != null ? errorMessage : e.getClass().getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            System.err.println("=== Exception Générale ===");
            System.err.println("Type: " + e.getClass().getName());
            System.err.println("Message: " + errorMessage);
            System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
            if (e.getCause() != null) {
                System.err.println("=== Stack Trace de la Cause ===");
                e.getCause().printStackTrace();
            }
            System.err.println("=== Stack Trace Complète ===");
            e.printStackTrace();
            return CResponse.error("Erreur lors de la suppression de l'apprenant: " + (errorMessage != null ? errorMessage : e.getClass().getSimpleName()));
        }
    }
    
    // Original methods to adapt/re-implement as needed
    public CResponse<?> getByCohorte(Long cohorteId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Optional<Cohorte> cohorteOptional = cohorteRepository.findById(cohorteId);
        if (cohorteOptional.isEmpty()) {
            return CResponse.error("Cohorte non trouvée avec l'ID: " + cohorteId);
        }
        Page<Apprenant> apprenants = apprenantRepository.findAllByActivateAndCohorteId(true, cohorteId, paging);
        return CResponse.success(apprenants, "Les apprenants de " + cohorteOptional.get().getNom());
    }
}