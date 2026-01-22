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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

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
            if (coursesOptional.isPresent()) {
                removeOldModuleToCourse(moduleAndCoursePayload.getCourseId());
                coursesOptional.get().setLevel(moduleAndCoursePayload.getCourseType());
                coursesRepository.save(coursesOptional.get());
                if(!moduleAndCoursePayload.getModules().isEmpty()) {
                    List<Module> moduleList = new ArrayList<>();
                    int totalLessons = 0;
                    
                    for (com.odc.aws_learning.app.dto.ModuleCreationRequest module : moduleAndCoursePayload.getModules()) {
                        System.out.println("=== Traitement du module: " + module.getTitle() + " ===");
                        System.out.println("Nombre de leçons dans le module: " + (module.getLessons() != null ? module.getLessons().size() : 0));
                        
                        Module moduleToSave = new Module();
                        moduleToSave.setCourse(coursesOptional.get());
                        moduleToSave.setDescription(module.getDescription());
                        moduleToSave.setTitle(module.getTitle());
                        moduleToSave.setModuleOrder(module.getModuleOrder());
                        moduleToSave.setActivate(true); // S'assurer que le module est activé

                        Module savedModule = moduleRepository.save(moduleToSave);
                        System.out.println("Module sauvegardé avec ID: " + savedModule.getId());
                        
                        // Si le module a des leçons, les sauvegarder
                        if (module.getLessons() != null && !module.getLessons().isEmpty()) {
                            System.out.println("Début de la sauvegarde des leçons pour le module: " + savedModule.getId());
                            List<Lesson> lessonsToSave = new ArrayList<>();
                            for (com.odc.aws_learning.app.dto.LessonCreationRequest lesson : module.getLessons()) {
                                System.out.println("Traitement de la leçon: " + lesson.getTitle() + ", Type: " + lesson.getType() + ", Order: " + lesson.getLessonOrder());
                                
                                Lesson lessonToSave = new Lesson();
                                lessonToSave.setTitle(lesson.getTitle());
                                lessonToSave.setLessonOrder(lesson.getLessonOrder());
                                lessonToSave.setType(lesson.getType()); // Le type doit être correctement défini
                                lessonToSave.setContentUrl(lesson.getContentUrl() != null && !lesson.getContentUrl().trim().isEmpty() ? lesson.getContentUrl() : null);
                                // La durée peut être null si non fournie, mais on essaie de la définir si elle existe
                                if (lesson.getDuration() != null && lesson.getDuration() > 0) {
                                    lessonToSave.setDuration(lesson.getDuration());
                                } else {
                                    // Si la durée n'est pas fournie, on peut la mettre à null ou 0
                                    lessonToSave.setDuration(null);
                                }
                                lessonToSave.setModule(savedModule);
                                lessonToSave.setActivate(true); // S'assurer que la leçon est activée
                                lessonsToSave.add(lessonToSave);
                                System.out.println("Leçon ajoutée à la liste: " + lessonToSave.getTitle());
                            }
                            if (!lessonsToSave.isEmpty()) {
                                System.out.println("Sauvegarde de " + lessonsToSave.size() + " leçons dans la base de données...");
                                try {
                                    List<Lesson> savedLessons = lessonRepository.saveAll(lessonsToSave);
                                    System.out.println("Leçons sauvegardées avec succès: " + savedLessons.size());
                                    totalLessons += savedLessons.size();
                                } catch (Exception e) {
                                    System.err.println("ERREUR lors de la sauvegarde des leçons: " + e.getMessage());
                                    e.printStackTrace();
                                    throw e; // Re-lancer l'exception pour qu'elle soit capturée par le catch principal
                                }
                            } else {
                                System.out.println("Aucune leçon à sauvegarder (liste vide)");
                            }
                        } else {
                            System.out.println("Aucune leçon dans ce module (lessons est null ou vide)");
                        }
                        
                        moduleList.add(savedModule);
                    }
                    
                    String message = String.format("Modules (%d) et leçons (%d) enregistrés avec succès", moduleList.size(), totalLessons);
                    return CResponse.success(moduleList.size(), message);
                }

                return CResponse.error("Attention, la liste des modules est vide");

            }
            return CResponse.error("Cours introuvable");

        } catch (Exception e) {
            e.printStackTrace(); // Log l'erreur pour le débogage
            return CResponse.error("Erreur d'enregistrement: " + e.getMessage());
        }
    }

    void removeOldModuleToCourse(Long courseId) {
        // Ne supprimer que les modules qui ont déjà des leçons (modules complets)
        // Les modules sans leçons seront simplement mis à jour avec leurs nouvelles leçons
        List<Module> allModules = moduleRepository.findAllByActivateAndCourseId(true, courseId);
        List<Module> modulesToRemove = new ArrayList<>();
        
        for (Module module : allModules) {
            // Supprimer seulement les modules qui ont déjà des leçons
            // Cela évite de supprimer les modules fraîchement créés sans leçons
            if (module.getLessons() != null && !module.getLessons().isEmpty()) {
                modulesToRemove.add(module);
            }
        }
        
        if (!modulesToRemove.isEmpty()) {
            System.out.println("Suppression de " + modulesToRemove.size() + " anciens modules avec leçons");
            // Supprimer les leçons associées d'abord
            for (Module module : modulesToRemove) {
                if (module.getLessons() != null && !module.getLessons().isEmpty()) {
                    lessonRepository.deleteAll(module.getLessons());
                }
            }
            // Ensuite supprimer les modules
            moduleRepository.deleteAll(modulesToRemove);
        } else {
            System.out.println("Aucun ancien module avec leçons à supprimer");
        }
    }

    public CResponse<?> getModulesByCourse(Long courseId, User user) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(courseId);
            if (coursesOptional.isEmpty()) {
                return CResponse.error("Cours introuvable");
            }

            // Vérifier si l'utilisateur est inscrit au cours (sauf pour ADMIN et INSTRUCTOR)
            if (user != null) {
                boolean isAdmin = user.getAdmin() != null;
                boolean isInstructor = user.getInstructor() != null;
                
                // Les admins et instructeurs peuvent voir tous les modules sans inscription
                if (!isAdmin && !isInstructor) {
                    Optional<com.odc.aws_learning.app.entity.DetailsCourse> enrollment = detailsCourseRepo
                            .findByCourseIdAndLearnerId(courseId, user.getId());
                    
                    if (enrollment.isEmpty() || !enrollment.get().isActivate()) {
                        return CResponse.error("Vous devez vous inscrire à ce cours pour accéder aux modules et leçons");
                    }
                }
            } else {
                // Utilisateur non authentifié
                return CResponse.error("Vous devez être authentifié et inscrit à ce cours pour accéder aux modules et leçons");
            }

            // Utiliser la méthode qui charge les leçons avec les modules
            List<Module> modules = moduleRepository.findAllByActivateAndCourseIdWithLessons(courseId);
            
            // DEBUG: Log pour vérifier le contentUrl des leçons
            System.out.println("=== DEBUG: Modules récupérés pour le cours " + courseId + " ===");
            System.out.println("Nombre de modules: " + modules.size());
            for (Module module : modules) {
                System.out.println("Module: " + module.getTitle() + " (ID: " + module.getId() + ")");
                if (module.getLessons() != null) {
                    System.out.println("  Nombre de leçons: " + module.getLessons().size());
                    for (Lesson lesson : module.getLessons()) {
                        System.out.println("  Leçon: " + lesson.getTitle() + 
                                         " (ID: " + lesson.getId() + 
                                         ", Type: " + lesson.getType() + 
                                         ", contentUrl: " + (lesson.getContentUrl() != null ? lesson.getContentUrl() : "NULL") + ")");
                    }
                } else {
                    System.out.println("  Aucune leçon dans ce module");
                }
            }
            System.out.println("=== FIN DEBUG ===");
            
            return CResponse.success(modules, "Modules");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur de récupération: " + e.getMessage());
        }
    }
}

