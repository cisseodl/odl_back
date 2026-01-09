package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.wrapper.ModuleAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
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

    public ModuleService(ModuleRepository moduleRepository, CoursesRepository coursesRepository, UploadFileService uploadFileService, LessonRepository lessonRepository) {
        this.moduleRepository = moduleRepository;
        this.coursesRepository = coursesRepository;
        this.uploadFileService = uploadFileService;
        this.lessonRepository = lessonRepository;
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
                    moduleAndCoursePayload.getModules().forEach(module -> {
                        Module moduleToSave = new Module();
                        moduleToSave.setCourse(coursesOptional.get());
                        moduleToSave.setDescription(module.getDescription());
                        moduleToSave.setTitle(module.getTitle());
                        moduleToSave.setModuleOrder(module.getModuleOrder());

                        Module savedModule = moduleRepository.save(moduleToSave);
                        
                        // Si le module a des leçons, les sauvegarder
                        if (module.getLessons() != null && !module.getLessons().isEmpty()) {
                            List<Lesson> lessonsToSave = new ArrayList<>();
                            module.getLessons().forEach(lesson -> {
                                Lesson lessonToSave = new Lesson();
                                lessonToSave.setTitle(lesson.getTitle());
                                lessonToSave.setLessonOrder(lesson.getLessonOrder());
                                lessonToSave.setType(lesson.getType());
                                lessonToSave.setContentUrl(lesson.getContentUrl());
                                lessonToSave.setDuration(lesson.getDuration());
                                lessonToSave.setModule(savedModule);
                                lessonsToSave.add(lessonToSave);
                            });
                            lessonRepository.saveAll(lessonsToSave);
                        }
                        
                        moduleList.add(savedModule);
                    });
                    return CResponse.success(moduleList.size(), "Modules et leçons enregistrés avec succès");
                }



                return CResponse.error("Attention, la liste des modules est vide");

            }
            return CResponse.error("Cours introuvable");

        } catch (Exception e) {
            return CResponse.error("Error d'enregistrement");
        }
    }

    void removeOldModuleToCourse(Long courseId) {
        List<Module> modulesToRemove = moduleRepository.findAllByActivateAndCourseId(true, courseId);
        if (!modulesToRemove.isEmpty()) {
            modulesToRemove.forEach(module -> {
                module.setCourse(null);
                moduleRepository.save(module);
            });
        }
    }

    public CResponse<?> getModulesByCourse(Long courseId) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(courseId);
            if (coursesOptional.isPresent()) {
                List<Module> modules = moduleRepository.findAllByActivateAndCourseId(true, courseId);
                return CResponse.success(modules, "Modules");
            }
            return CResponse.error("Cours introuvable");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }
}

