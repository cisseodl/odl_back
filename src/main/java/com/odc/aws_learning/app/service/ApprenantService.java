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
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.app.wrapper.ApprenantWithUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.odc.aws_learning.app.service.SendEmailService;

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
    private final SendEmailService sendEmailService;
    // private final PasswordEncoder passwordEncoder; // Removed
    
    @Value("${app.frontend.url:https://admin.smart-odc.com}")
    private String frontendUrl;

    @Transactional
    public CResponse<?> createApprenantAuthenticated(String emailFromJwt, ApprenantCreateRequest request) {
        User user;
        
        // Si userId est fourni, utiliser cet ID (pour permettre aux admins de créer un apprenant pour un autre utilisateur)
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + request.getUserId()));
        } else if (request.getUserEmail() != null) {
            // Si userEmail est fourni, utiliser cet email
            user = userRepository.findByEmail(request.getUserEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email: " + request.getUserEmail()));
        } else {
            // Sinon, utiliser l'email du JWT (comportement par défaut)
            user = userRepository.findByEmail(emailFromJwt)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        }

        if (apprenantRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un apprenant.");
        }

        Apprenant apprenant = new Apprenant();
        apprenant.setActivate(request.getActivate() != null ? request.getActivate() : true);
        
        // Utiliser le username pour mettre à jour le fullName du User
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            user.setFullName(request.getUsername().trim());
        }
        
        // L'email et le fullName viennent du User, pas besoin de les stocker dans Apprenant
        // Mais on garde nom, prenom, email dans Apprenant pour compatibilité avec l'entité existante
        // Ces champs seront synchronisés avec le User
        if (user.getFullName() != null) {
            String[] nameParts = user.getFullName().trim().split("\\s+", 2);
            if (nameParts.length >= 2) {
                apprenant.setPrenom(nameParts[0]);
                apprenant.setNom(nameParts[1]);
            } else {
                apprenant.setPrenom(nameParts[0]);
                apprenant.setNom("");
            }
        }
        apprenant.setEmail(user.getEmail()); // Utiliser l'email du User
        apprenant.setNumero(request.getNumero()); // Le numéro vient du DTO
        apprenant.setProfession(request.getProfession());
        apprenant.setNiveauEtude(request.getNiveauEtude());
        apprenant.setFiliere(request.getFiliere());
        apprenant.setAttentes(request.getAttentes());
        apprenant.setSatisfaction(request.getSatisfaction());
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

        // Envoyer un email uniquement si l'apprenant est créé par un admin (userId fourni dans la requête)
        if (request.getUserId() != null) {
            try {
                String emailMessage = sendEmailService.mailTemplateApprenantCreated(
                    user.getFullName() != null ? user.getFullName() : user.getEmail(),
                    user.getEmail(),
                    frontendUrl
                );
                sendEmailService.sendEmailWithAttachment(
                    user.getEmail(),
                    emailMessage,
                    "Votre compte apprenant a été créé - Orange Digital Learning"
                );
            } catch (Exception e) {
                // Ne pas faire échouer la création si l'email échoue
                System.err.println("Erreur lors de l'envoi de l'email à l'apprenant: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return CResponse.success(savedApprenant, "Apprenant créé avec succès.");
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
            List<ApprenantWithUserDto> apprenantDtos = apprenants.stream()
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
            
            return CResponse.success(apprenantDtos, "Liste des apprenants.");
        } catch (Exception e) {
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

    @Transactional
    public CResponse<?> updateApprenant(Long id, User userDetails, String username, String numero, String profession, String niveauEtude, String filiere, Long cohorteId, String attentes, Boolean satisfaction) {
        return apprenantRepository.findById(id)
                .map(apprenant -> {
                    // Update user details if necessary (e.g., from a DTO)
                    User user = apprenant.getUser();
                    if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
                    if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
                    // ... update other user fields as needed ...
                    userRepository.save(user); // Save updated user details
                    
                    // Utiliser le username pour mettre à jour le fullName du User
                    if (username != null && !username.trim().isEmpty()) {
                        user.setFullName(username.trim());
                    }
                    
                    // Mettre à jour l'email de l'apprenant avec celui du User
                    apprenant.setEmail(user.getEmail());

                    // Synchroniser nom et prenom de l'apprenant avec le fullName du User
                    if (user.getFullName() != null) {
                        String[] nameParts = user.getFullName().trim().split("\\s+", 2);
                        if (nameParts.length >= 2) {
                            apprenant.setPrenom(nameParts[0]);
                            apprenant.setNom(nameParts[1]);
                        } else {
                            apprenant.setPrenom(nameParts[0]);
                            apprenant.setNom("");
                        }
                    }
                    
                    if (numero != null) apprenant.setNumero(numero);
                    if (profession != null) apprenant.setProfession(profession);
                    if (niveauEtude != null) apprenant.setNiveauEtude(niveauEtude);
                    if (filiere != null) apprenant.setFiliere(filiere);
                    if (attentes != null) apprenant.setAttentes(attentes);
                    if (satisfaction != null) apprenant.setSatisfaction(satisfaction);

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
            
            // Supprimer d'abord les DetailsCourse (inscriptions aux cours) associés à cet utilisateur
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
            }
            
            // Supprimer les UserQuizAttempt car cette relation n'a pas de cascade depuis User
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
            }
            
            System.out.println("Suppression de l'utilisateur...");
            // Supprimer l'utilisateur, ce qui supprimera automatiquement l'Apprenant en cascade
            // grâce à CascadeType.ALL et orphanRemoval = true dans la relation User -> Apprenant
            userRepository.delete(user);
            System.out.println("Utilisateur supprimé avec succès");
            
            return CResponse.success(null, "Apprenant et utilisateur associé supprimés avec succès.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            System.err.println("DataIntegrityViolationException: " + errorMessage);
            if (errorMessage != null && (errorMessage.contains("foreign key constraint") || errorMessage.contains("constraint"))) {
                return CResponse.error("Impossible de supprimer l'apprenant car il est référencé par d'autres données. Veuillez d'abord supprimer les données associées.");
            }
            return CResponse.error("Erreur de contrainte lors de la suppression de l'apprenant: " + errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            System.err.println("Exception lors de la suppression: " + errorMessage);
            System.err.println("Type d'exception: " + e.getClass().getName());
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