package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.wrapper.ModuleAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import com.odc.aws_learning.app.entity.Categorie; // Import for Categorie
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ModuleService {

    private static final Logger log = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;
    private final CoursesRepository coursesRepository;
    private final UploadFileService uploadFileService;
    private final LessonRepository lessonRepository;
    private final DetailsCourseRepo detailsCourseRepo;

    public ModuleService(ModuleRepository moduleRepository, CoursesRepository coursesRepository, UploadFileService uploadFileService, LessonRepository lessonRepository, DetailsCourseRepo detailsCourseRepo) {
        this.moduleRepository = moduleRepository;
        this.coursesRepository = coursesRepository;
        this.uploadFileService = uploadFileService;
        this.lessonRepository = lessonRepository;
        this.detailsCourseRepo = detailsCourseRepo;
    }

    public CResponse<?> saveModule(ModuleAndCoursePayload moduleAndCoursePayload, MultipartFile pdfFile) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(moduleAndCoursePayload.getCourseId());
            if (coursesOptional.isEmpty()) {
                return CResponse.error("Cours introuvable avec l'ID: " + moduleAndCoursePayload.getCourseId());
            }
            
            Courses courseToUpdate = coursesOptional.get();
            
            // Vérification robuste de la catégorie du cours
            Categorie associatedCategory = courseToUpdate.getCategorie();
            if (associatedCategory == null) {
                return CResponse.error("Le cours avec l'ID " + courseToUpdate.getId() + " n'a pas de catégorie associée (objet Categorie est null). Veuillez assigner une catégorie avant de modifier ses modules.");
            }
            if (associatedCategory instanceof HibernateProxy) {
                try {
                    Hibernate.initialize(associatedCategory);
                } catch (Exception hibernateEx) {
                    return CResponse.error("Le cours avec l'ID " + courseToUpdate.getId() + " a une catégorie non initialisable. Veuillez assigner une catégorie valide avant de modifier ses modules. Détails: " + hibernateEx.getMessage());
                }
            }
            if (associatedCategory.getId() == null || associatedCategory.getId() == 0L) {
                 return CResponse.error("Le cours avec l'ID " + courseToUpdate.getId() + " a une catégorie associée avec un ID invalide (null ou 0). Veuillez assigner une catégorie valide avant de modifier ses modules.");
            }
            
            // Mise à jour du niveau du cours
            // coursesOptional.get().setLevel(moduleAndCoursePayload.getCourseType()); // Désactivé - Niveau doit être mis à jour dans CoursesController ou CourseService

            // Effacer les anciens modules pour que orphanRemoval=true fonctionne
            // Important : Charger explicitement les modules si FetchType.LAZY pour éviter ConcurrentModificationException
            if (courseToUpdate.getModules() != null) {
                Hibernate.initialize(courseToUpdate.getModules());
                courseToUpdate.getModules().clear(); // Supprime tous les modules et leurs leçons associées (orphanRemoval)
            } else {
                courseToUpdate.setModules(new ArrayList<>());
            }

            int totalLessonsCount = 0;
            // Construire les nouveaux modules et leçons
            for (com.odc.aws_learning.app.dto.ModuleCreationRequest moduleRequest : moduleAndCoursePayload.getModules()) {
                Module newModule = new Module();
                newModule.setCreatedAt(LocalDateTime.now()); // Explicitly set creation timestamp
                newModule.setLastModifiedAt(LocalDateTime.now()); // Explicitly set modification timestamp
                newModule.setTitle(moduleRequest.getTitle());
                newModule.setDescription(moduleRequest.getDescription());
                newModule.setModuleOrder(moduleRequest.getModuleOrder());
                newModule.setActivate(true); // Module actif par défaut
                newModule.setCourse(courseToUpdate); // Lier le module au cours

                if (moduleRequest.getLessons() != null && !moduleRequest.getLessons().isEmpty()) {
                    List<Lesson> lessonsForModule = new ArrayList<>();
                    for (com.odc.aws_learning.app.dto.LessonCreationRequest lessonRequest : moduleRequest.getLessons()) {
                        Lesson newLesson = new Lesson();
                        newLesson.setCreatedAt(LocalDateTime.now()); // Explicitly set creation timestamp
                        newLesson.setLastModifiedAt(LocalDateTime.now()); // Explicitly set modification timestamp
                        newLesson.setTitle(lessonRequest.getTitle());
                        newLesson.setLessonOrder(lessonRequest.getLessonOrder());
                        newLesson.setType(lessonRequest.getType());
                        newLesson.setContentUrl(lessonRequest.getContentUrl());
                        newLesson.setDuration(lessonRequest.getDuration());
                        newLesson.setActivate(true); // Leçon active par défaut
                        newLesson.setModule(newModule); // Lier la leçon au module
                        lessonsForModule.add(newLesson);
                        totalLessonsCount++;
                    }
                    newModule.setLessons(lessonsForModule); // Ajouter toutes les leçons au module
                }
                courseToUpdate.getModules().add(newModule); // Ajouter le nouveau module à la liste du cours
            }
            
            // Sauvegarder le cours une seule fois pour persister toutes les modifications en cascade
            coursesRepository.save(courseToUpdate);
            
            String message = String.format("Modules (%d) et leçons (%d) enregistrés avec succès pour le cours ID %d", 
                                           moduleAndCoursePayload.getModules().size(), totalLessonsCount, courseToUpdate.getId());
            return CResponse.success(moduleAndCoursePayload.getModules().size(), message);

        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement des modules et leçons : {}", e.getMessage(), e);
            return CResponse.error("Erreur d'enregistrement des modules/leçons : " + e.getMessage());
        }
    }

    // L'ancienne méthode removeOldModuleToCourse n'est plus nécessaire avec orphanRemoval=true et clear()
    // Je la laisse commentée si jamais elle est utile pour d'autres scénarios, mais elle ne sera pas appelée.
    // void removeOldModuleToCourse(Long courseId) { ... }

    public CResponse<?> getModulesByCourse(Long courseId, User user) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(courseId);
            if (coursesOptional.isEmpty()) {
                return CResponse.error("Cours introuvable");
            }

            // VÉRIFICATION STRICTE D'INSCRIPTION
            // IMPORTANT : Si l'utilisateur est authentifié (même si getCurrentUser() retourne null),
            // il DOIT être inscrit pour voir les modules
            // Vérifier l'authentification via SecurityContextHolder directement
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            log.info("=== VÉRIFICATION INSCRIPTION pour cours {} ===", courseId);
            log.info("Authentication: {}", authentication != null ? "présent" : "null");
            if (authentication != null) {
                log.info("Authentication.isAuthenticated(): {}", authentication.isAuthenticated());
                log.info("Authentication.principal type: {}", authentication.getPrincipal() != null ? authentication.getPrincipal().getClass().getName() : "null");
                if (authentication.getPrincipal() instanceof String) {
                    log.info("Authentication.principal value: {}", authentication.getPrincipal());
                }
            }
            log.info("User from getCurrentUser(): {}", user != null ? "présent (ID: " + user.getId() + ")" : "null");
            
            boolean isAuthenticated = authentication != null && 
                                    authentication.isAuthenticated() && 
                                    !(authentication.getPrincipal() instanceof String && 
                                      "anonymousUser".equals(authentication.getPrincipal()));
            
            log.info("isAuthenticated calculé: {}", isAuthenticated);
            
            if (isAuthenticated) {
                // L'utilisateur est authentifié (via token JWT)
                log.info("Utilisateur authentifié détecté pour le cours {}", courseId);
                
                // Si getCurrentUser() retourne null, c'est un problème, mais on doit quand même vérifier l'inscription
                if (user == null) {
                    log.error("❌ ERREUR: Utilisateur authentifié mais getCurrentUser() retourne null pour le cours {}", courseId);
                    // Si l'utilisateur est authentifié mais qu'on ne peut pas le récupérer, 
                    // on ne peut pas vérifier l'inscription → retourner une erreur
                    return CResponse.error("Erreur d'authentification. Veuillez vous reconnecter.");
                }
                
                log.info("Vérification de l'inscription pour User ID: {} et Course ID: {}", user.getId(), courseId);
                
                // Vérifier si l'utilisateur est inscrit à ce cours
                Optional<com.odc.aws_learning.app.entity.DetailsCourse> enrollment = 
                    detailsCourseRepo.findByCourseIdAndLearnerId(courseId, user.getId());
                
                if (enrollment.isEmpty()) {
                    log.warn("❌ INSCRIPTION: Aucune inscription trouvée pour User ID: {} et Course ID: {}", user.getId(), courseId);
                    return CResponse.error("Vous devez vous inscrire à ce cours pour accéder aux modules");
                }
                
                if (!enrollment.get().isActivate()) {
                    log.warn("❌ INSCRIPTION: Inscription désactivée pour User ID: {} et Course ID: {}", user.getId(), courseId);
                    return CResponse.error("Vous devez vous inscrire à ce cours pour accéder aux modules");
                }
                
                log.info("✅ INSCRIPTION: Utilisateur inscrit et actif pour User ID: {} et Course ID: {}", user.getId(), courseId);
            } else {
                log.info("Utilisateur NON authentifié - consultation publique autorisée pour le cours {}", courseId);
            }
            // Si l'utilisateur n'est pas authentifié, permettre la consultation publique
            // (pour la page /courses/id qui affiche les détails du cours)

            // Utiliser la méthode qui charge les leçons avec les modules
            List<Module> modules = moduleRepository.findAllByActivateAndCourseIdWithLessons(courseId);
            
            // DEBUG: Log pour vérifier le contentUrl des leçons
            // Ces logs s'affichent dans la console du serveur backend (pas dans le navigateur)
            log.info("=== DEBUG: Modules récupérés pour le cours {} ===", courseId);
            log.info("Nombre de modules: {}", modules.size());
            for (Module module : modules) {
                log.info("Module: {} (ID: {})", module.getTitle(), module.getId());
                if (module.getLessons() != null) {
                    log.info("  Nombre de leçons: {}", module.getLessons().size());
                    for (Lesson lesson : module.getLessons()) {
                        String contentUrl = lesson.getContentUrl() != null ? lesson.getContentUrl() : "NULL";
                        log.info("  Leçon: {} (ID: {}, Type: {}, contentUrl: {})", 
                                lesson.getTitle(), lesson.getId(), lesson.getType(), contentUrl);
                    }
                } else {
                    log.info("  Aucune leçon dans ce module");
                }
            }
            log.info("=== FIN DEBUG ===");
            
            return CResponse.success(modules, "Modules");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur de récupération: " + e.getMessage());
        }
    }
}