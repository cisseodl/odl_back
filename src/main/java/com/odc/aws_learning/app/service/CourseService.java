package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.app.constante.CourseStatus;
import com.odc.aws_learning.app.dto.CourseCreationRequest;
import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.dto.CourseUpdateRequest;
import com.odc.aws_learning.app.dto.ModuleCreationRequest;
import com.odc.aws_learning.app.dto.ModuleUpdateRequest;
import com.odc.aws_learning.app.dto.LessonCreationRequest;
import com.odc.aws_learning.app.dto.LessonUpdateRequest;
import com.odc.aws_learning.app.entity.Categorie;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.DetailsCourse; // Added
import com.odc.aws_learning.app.entity.Module; // Added
import com.odc.aws_learning.app.entity.Lesson; // Added
import com.odc.aws_learning.auth.base.response.CResponse; // Added
import com.odc.aws_learning.app.mapper.CourseMapper;
import com.odc.aws_learning.app.mapper.InstructorMapper;
import com.odc.aws_learning.app.repository.CategorieRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.InstructorProfileRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.repository.ReviewRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.repository.CertificateRepository; // Added
import com.odc.aws_learning.app.service.NotificationService;
import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private final CoursesRepository coursesRepository;
    private final CourseMapper courseMapper;
    private final InstructorMapper instructorMapper;
    private final ReviewRepository reviewRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;
    private final CategorieRepository categorieRepository;
    private final InstructorProfileRepository instructorProfileRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CertificateRepository certificateRepository; // Injected
    private final NotificationService notificationService;
    private final SendEmailService sendEmailService;

    public CourseService(CoursesRepository coursesRepository, CourseMapper courseMapper, InstructorMapper instructorMapper, ReviewRepository reviewRepository, UserProgressRepository userProgressRepository, UserRepository userRepository, CategorieRepository categorieRepository, InstructorProfileRepository instructorProfileRepository, DetailsCourseRepo detailsCourseRepo, ModuleRepository moduleRepository, LessonRepository lessonRepository, CertificateRepository certificateRepository, NotificationService notificationService, SendEmailService sendEmailService) {
        this.coursesRepository = coursesRepository;
        this.courseMapper = courseMapper;
        this.instructorMapper = instructorMapper;
        this.reviewRepository = reviewRepository;
        this.userProgressRepository = userProgressRepository;
        this.userRepository = userRepository;
        this.categorieRepository = categorieRepository;
        this.instructorProfileRepository = instructorProfileRepository;
        this.detailsCourseRepo = detailsCourseRepo;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.certificateRepository = certificateRepository;
        this.notificationService = notificationService;
        this.sendEmailService = sendEmailService;
    }

    public CourseDto getCourseById(Long id, User user) {
        try {
            // Charger le cours avec ses relations de base (instructor, categorie)
            Courses course = coursesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

            // Vérifier si l'utilisateur est inscrit au cours (sauf pour ADMIN et INSTRUCTOR)
            boolean canAccessModules = false;
            if (user != null) {
                try {
                    boolean isAdmin = user.getAdmin() != null;
                    boolean isInstructor = user.getInstructor() != null;
                    
                    // Les admins et instructeurs peuvent voir tous les modules sans inscription
                    if (isAdmin || isInstructor) {
                        canAccessModules = true;
                    } else {
                        // Vérifier l'inscription pour les autres utilisateurs
                        Optional<DetailsCourse> enrollment = detailsCourseRepo
                                .findByCourseIdAndLearnerId(id, user.getId());
                        canAccessModules = enrollment.isPresent() && enrollment.get().isActivate();
                    }
                } catch (Exception e) {
                    System.err.println("Error checking enrollment for user " + user.getId() + " and course " + id + ": " + e.getMessage());
                    e.printStackTrace();
                    // En cas d'erreur, on considère que l'utilisateur n'est pas inscrit
                    canAccessModules = false;
                }
            }

            // Charger explicitement les modules avec leurs leçons seulement si l'utilisateur est inscrit
            if (canAccessModules) {
                try {
                    List<Module> modules = moduleRepository.findAllByActivateAndCourseIdWithLessons(id);
                    if (modules != null && !modules.isEmpty()) {
                        // Trier les modules par moduleOrder
                        modules.sort((m1, m2) -> {
                            Integer order1 = m1.getModuleOrder() != null ? m1.getModuleOrder() : Integer.MAX_VALUE;
                            Integer order2 = m2.getModuleOrder() != null ? m2.getModuleOrder() : Integer.MAX_VALUE;
                            return order1.compareTo(order2);
                        });
                        // Trier les leçons de chaque module par lessonOrder
                        modules.forEach(module -> {
                            if (module.getLessons() != null && !module.getLessons().isEmpty()) {
                                module.getLessons().sort((l1, l2) -> {
                                    Integer order1 = l1.getLessonOrder() != null ? l1.getLessonOrder() : Integer.MAX_VALUE;
                                    Integer order2 = l2.getLessonOrder() != null ? l2.getLessonOrder() : Integer.MAX_VALUE;
                                    return order1.compareTo(order2);
                                });
                            }
                        });
                        course.setModules(modules);
                    } else {
                        course.setModules(new ArrayList<>());
                    }
                } catch (Exception e) {
                    System.err.println("Error loading modules for course " + id + ": " + e.getMessage());
                    e.printStackTrace();
                    // En cas d'erreur, on met une liste vide
                    course.setModules(new ArrayList<>());
                }
            } else {
                // Ne pas charger les modules si l'utilisateur n'est pas inscrit
                course.setModules(new ArrayList<>());
            }

            CourseDto courseDto = courseMapper.toDto(course);
            populateCalculatedFields(courseDto, course);
            return courseDto;
        } catch (RuntimeException e) {
            // Re-lancer les RuntimeException (comme Course not found)
            throw e;
        } catch (Exception e) {
            System.err.println("Error in getCourseById for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            // Retourner un cours minimal plutôt que de faire échouer la requête
            try {
                Courses course = coursesRepository.findById(id).orElse(null);
                if (course != null) {
                    course.setModules(new ArrayList<>());
                    CourseDto courseDto = courseMapper.toDto(course);
                    populateCalculatedFields(courseDto, course);
                    return courseDto;
                }
            } catch (Exception e2) {
                System.err.println("Error creating minimal course DTO: " + e2.getMessage());
            }
            throw new RuntimeException("Erreur lors de la récupération du cours: " + e.getMessage(), e);
        }
    }

    public List<CourseDto> getAllCourses(
            String category, CourseLevel level, String language, Boolean bestseller, CourseStatus status,
            int page, int size, String sortBy, String sortDir) {
        try {
            // Utiliser les méthodes avec FETCH pour charger les relations nécessaires
            List<Courses> coursesList;
            
            // Basic filtering logic avec jointures FETCH
            if (status != null) {
                coursesList = this.coursesRepository.findByStatusWithRelations(status);
            } else if (category != null && !category.isEmpty()) {
                // Find category by title (needs a method in CategorieRepository)
                Optional<Categorie> cat = this.categorieRepository.findByTitle(category);
                if (cat.isPresent()) {
                    coursesList = this.coursesRepository.findByCategorieIdWithRelations(cat.get().getId());
                } else {
                    coursesList = new ArrayList<>();
                }
            } else if (level != null) {
                coursesList = this.coursesRepository.findByLevelWithRelations(level);
            } else if (bestseller != null) {
                coursesList = this.coursesRepository.findByBestsellerWithRelations(bestseller);
            } else {
                coursesList = this.coursesRepository.findAllWithRelations();
            }
            
            // Appliquer la pagination manuellement après avoir récupéré les données avec les relations
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            
            // Trier la liste
            List<Courses> sortedList = coursesList.stream()
                    .sorted((c1, c2) -> {
                        Comparable val1 = getFieldValue(c1, sortBy);
                        Comparable val2 = getFieldValue(c2, sortBy);
                        if (val1 == null && val2 == null) return 0;
                        if (val1 == null) return 1;
                        if (val2 == null) return -1;
                        int comparison = val1.compareTo(val2);
                        return sortDir.equalsIgnoreCase("ASC") ? comparison : -comparison;
                    })
                    .collect(Collectors.toList());
            
            // Appliquer la pagination
            int start = page * size;
            int end = Math.min(start + size, sortedList.size());
            List<Courses> paginatedList = start < sortedList.size() ? 
                    sortedList.subList(start, end) : new ArrayList<>();
            
            // Mapper les cours en DTOs
            // La durée est maintenant calculée à partir du champ duration du cours (en secondes)
            return paginatedList.stream()
                    .map(course -> {
                        try {
                            CourseDto dto = this.courseMapper.toDto(course);
                            if (dto != null) {
                                populateCalculatedFields(dto, course);
                            }
                            return dto;
                        } catch (Exception e) {
                            System.err.println("Error processing course " + (course != null ? course.getId() : "null") + ": " + e.getMessage());
                            e.printStackTrace();
                            // Essayer de retourner un DTO minimal en cas d'erreur
                            try {
                                CourseDto dto = this.courseMapper.toDto(course);
                                return dto;
                            } catch (Exception e2) {
                                System.err.println("Critical error: Cannot create DTO for course " + (course != null ? course.getId() : "null") + ": " + e2.getMessage());
                                e2.printStackTrace();
                                // Retourner null et filtrer plus tard
                                return null;
                            }
                        }
                    })
                    .filter(dto -> dto != null) // Filtrer les DTOs null
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getAllCourses: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des cours: " + e.getMessage(), e);
        }
    }
    
    // Helper method pour obtenir la valeur d'un champ pour le tri
    @SuppressWarnings("rawtypes")
    private Comparable getFieldValue(Courses course, String fieldName) {
        try {
            switch (fieldName.toLowerCase()) {
                case "id":
                    return course.getId();
                case "title":
                    return course.getTitle() != null ? course.getTitle() : "";
                case "createdat":
                case "created_at":
                    return course.getCreatedAt() != null ? course.getCreatedAt() : null;
                case "lastmodifiedat":
                case "last_modified_at":
                    return course.getLastModifiedAt() != null ? course.getLastModifiedAt() : null;
                default:
                    return course.getId();
            }
        } catch (Exception e) {
            return course.getId();
        }
    }

    public List<CourseDto> getCoursesByInstructorId(Long instructorId) {
        try {
            List<Courses> courses = coursesRepository.findByInstructor_Id(instructorId);
            return courses.stream()
                    .map(course -> {
                        try {
                            CourseDto dto = courseMapper.toDto(course);
                            populateCalculatedFields(dto, course);
                            return dto;
                        } catch (Exception e) {
                            System.err.println("Error processing course " + course.getId() + ": " + e.getMessage());
                            e.printStackTrace();
                            // Retourner un DTO minimal en cas d'erreur
                            CourseDto dto = courseMapper.toDto(course);
                            return dto;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getCoursesByInstructorId: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des cours de l'instructeur: " + e.getMessage(), e);
        }
    }

    public CourseDto createCourse(CourseCreationRequest request) {
        try {
            Courses course = new Courses();
            course.setTitle(request.getTitle());
            course.setSubtitle(request.getSubtitle());
            course.setDescription(request.getDescription());
            course.setImagePath(request.getImagePath());
            course.setLevel(request.getLevel());
            course.setLanguage(request.getLanguage());
            course.setStatus(CourseStatus.BROUILLON); // Définir le statut par défaut

            course.setObjectives(request.getObjectives());
            course.setFeatures(request.getFeatures());
            course.setCreatedAt(LocalDateTime.now()); // Set creation date

            User instructor = userRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + request.getInstructorId()));
            course.setInstructor(instructor);

            Categorie category = categorieRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            course.setCategorie(category);

            Courses savedCourse = coursesRepository.save(course);

        // Enregistrer les modules et leçons associées
        if (request.getModules() != null && !request.getModules().isEmpty()) {
            request.getModules().forEach(moduleRequest -> {
                Module module = new Module();
                module.setTitle(moduleRequest.getTitle());
                module.setDescription(moduleRequest.getDescription());
                module.setModuleOrder(moduleRequest.getModuleOrder());
                module.setCourse(savedCourse); // Lier au cours sauvegardé
                Module savedModule = moduleRepository.save(module);

                if (moduleRequest.getLessons() != null && !moduleRequest.getLessons().isEmpty()) {
                    moduleRequest.getLessons().forEach(lessonRequest -> {
                        Lesson lesson = new Lesson();
                        lesson.setTitle(lessonRequest.getTitle());
                        lesson.setLessonOrder(lessonRequest.getLessonOrder());
                        lesson.setType(lessonRequest.getType());
                        lesson.setContentUrl(lessonRequest.getContentUrl());
                        lesson.setDuration(lessonRequest.getDuration());
                        lesson.setModule(savedModule); // Lier au module sauvegardé
                        lessonRepository.save(lesson);
                    });
                }
            });
        }
        return courseMapper.toDto(savedCourse); // Calculated fields will be null here
        } catch (RuntimeException e) {
            throw e; // Re-throw RuntimeException avec le message d'erreur
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cours: " + e.getMessage(), e);
        }
    }

    public CourseDto updateCourse(Long id, CourseUpdateRequest request) {
        Courses existingCourse = coursesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        existingCourse.setTitle(request.getTitle());
        existingCourse.setSubtitle(request.getSubtitle());
        existingCourse.setDescription(request.getDescription());
        existingCourse.setImagePath(request.getImagePath());
        existingCourse.setLevel(request.getLevel());
        existingCourse.setLanguage(request.getLanguage());
                existingCourse.setObjectives(request.getObjectives());
        existingCourse.setFeatures(request.getFeatures());
        existingCourse.setBestseller(request.getBestseller());
        existingCourse.setLastModifiedAt(LocalDateTime.now()); // Set modification date

        if (request.getStatus() != null) {
            existingCourse.setStatus(request.getStatus());
        }

        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        existingCourse.setInstructor(instructor);

        Categorie category = categorieRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingCourse.setCategorie(category);

        Courses updatedCourse = coursesRepository.save(existingCourse);

        // Mettre à jour les modules et leçons associées
        if (request.getModules() != null) {
            // Identifier les IDs de modules à garder (ceux qui sont dans la requête)
            List<Long> moduleIdsToKeep = request.getModules().stream()
                    .map(ModuleUpdateRequest::getId)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            // Supprimer les modules existants qui ne sont PAS dans la requête
            existingCourse.getModules().removeIf(existingModule -> !moduleIdsToKeep.contains(existingModule.getId()));

            request.getModules().forEach(moduleRequest -> {
                Module module;
                if (moduleRequest.getId() != null) {
                    module = moduleRepository.findById(moduleRequest.getId())
                            .orElse(new Module()); // Créer si non trouvé
                    module.setId(moduleRequest.getId()); // S'assurer que l'ID est défini pour les updates
                } else {
                    module = new Module();
                }

                module.setTitle(moduleRequest.getTitle());
                module.setDescription(moduleRequest.getDescription());
                module.setModuleOrder(moduleRequest.getModuleOrder());
                module.setCourse(existingCourse); // Lier au cours existant

                Module savedModule = moduleRepository.save(module);

                // Mettre à jour les leçons
                if (moduleRequest.getLessons() != null) {
                    // Identifier les IDs de leçons à garder (ceux qui sont dans la requête)
                    List<Long> lessonIdsToKeep = moduleRequest.getLessons().stream()
                            .map(LessonUpdateRequest::getId)
                            .filter(java.util.Objects::nonNull)
                            .collect(Collectors.toList());

                    // Supprimer les leçons existantes qui ne sont PAS dans la requête
                    savedModule.getLessons().removeIf(existingLesson -> !lessonIdsToKeep.contains(existingLesson.getId()));

                    moduleRequest.getLessons().forEach(lessonRequest -> {
                        Lesson lesson;
                        if (lessonRequest.getId() != null) {
                            lesson = lessonRepository.findById(lessonRequest.getId())
                                    .orElse(new Lesson()); // Créer si non trouvée
                            lesson.setId(lessonRequest.getId()); // S'assurer que l'ID est défini pour les updates
                        } else {
                            lesson = new Lesson();
                        }

                        lesson.setTitle(lessonRequest.getTitle());
                        lesson.setLessonOrder(lessonRequest.getLessonOrder());
                        lesson.setType(lessonRequest.getType());
                        lesson.setContentUrl(lessonRequest.getContentUrl());
                        lesson.setDuration(lessonRequest.getDuration());
                        lesson.setModule(savedModule); // Lier au module
                        lessonRepository.save(lesson);
                    });
                }
            });
        }
        return courseMapper.toDto(updatedCourse);
    }

    public CourseDto validateCourse(Long courseId, com.odc.aws_learning.app.dto.CourseValidationRequest request) {
        return validateCourse(courseId, request, false);
    }

    public CourseDto validateCourse(Long courseId, com.odc.aws_learning.app.dto.CourseValidationRequest request, boolean isInstructorOwner) {
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        switch (request.getAction()) {
            case APPROVE:
                // Si l'instructeur est propriétaire, il peut valider directement (BROUILLON -> PUBLIE)
                // Sinon, le flux normal : BROUILLON -> IN_REVIEW -> PUBLIE
                if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.BROUILLON) {
                    if (isInstructorOwner) {
                        // L'instructeur propriétaire peut publier directement son cours
                        course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.PUBLIE);
                    } else {
                        // Sinon, passer en révision (pour validation par admin)
                        course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.IN_REVIEW);
                    }
                    course.setRejectionReason(null);
                } else if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.IN_REVIEW) {
                    // Seul l'admin peut approuver un cours en révision
                    course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.PUBLIE);
                    course.setRejectionReason(null);
                } else {
                    throw new IllegalArgumentException("Le cours ne peut pas être approuvé dans son état actuel.");
                }
                break;
            case REJECT:
                if (request.getReason() == null || request.getReason().trim().isEmpty()) {
                    throw new IllegalArgumentException("Rejection reason cannot be empty when rejecting a course.");
                }
                // Si le cours est en IN_REVIEW, l'admin le rejette (BROUILLON)
                // Si le cours est en BROUILLON, l'instructeur le retire de la validation (reste BROUILLON)
                if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.IN_REVIEW) {
                    course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.BROUILLON);
                    course.setRejectionReason(request.getReason());
                } else if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.BROUILLON) {
                    // L'instructeur retire son cours de la validation, reste en BROUILLON
                    course.setRejectionReason(request.getReason());
                } else {
                    throw new IllegalArgumentException("Le cours ne peut pas être rejeté dans son état actuel.");
                }
                break;
        }

        Courses savedCourse = coursesRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    public void deleteCourse(Long id) {
        coursesRepository.deleteById(id);
    }
    private void populateCalculatedFields(CourseDto dto, Courses course) {
        try {
            // Calculate rating and review count
            Double avgRating = null;
            Long reviewCount = null;
            try {
                avgRating = reviewRepository.findAverageRatingByCourse(course);
                reviewCount = reviewRepository.countByCourse(course);
            } catch (Exception e) {
                System.err.println("Error calculating rating/review count for course " + course.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
            dto.setRating(avgRating != null ? avgRating : 0.0);
            dto.setReviewCount(reviewCount != null ? reviewCount.intValue() : 0);

            // Calculate enrolled count
            long enrolledCount = 0;
            try {
                enrolledCount = userProgressRepository.countDistinctUsersByCourse(course);
            } catch (Exception e) {
                System.err.println("Error calculating enrolled count for course " + course.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
            dto.setEnrolledCount(enrolledCount);

            // Populate InstructorDto
            if (course.getInstructor() != null) {
                try {
                    instructorProfileRepository.findByUser(course.getInstructor()).ifPresent(
                        profile -> {
                            try {
                                dto.setInstructor(instructorMapper.toDto(course.getInstructor(), profile));
                            } catch (Exception e) {
                                System.err.println("Error mapping instructor for course " + course.getId() + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    );
                    // Si le profil instructeur n'existe pas, créer un InstructorDto minimal avec les infos de base
                    if (dto.getInstructor() == null) {
                        try {
                            com.odc.aws_learning.app.dto.InstructorDto minimalInstructor = new com.odc.aws_learning.app.dto.InstructorDto();
                            minimalInstructor.setId(course.getInstructor().getId());
                            minimalInstructor.setName(course.getInstructor().getFullName() != null ? 
                                    course.getInstructor().getFullName() : 
                                    course.getInstructor().getEmail() != null ? course.getInstructor().getEmail() : 
                                    "Instructor #" + course.getInstructor().getId());
                            minimalInstructor.setAvatar(course.getInstructor().getAvatar());
                            dto.setInstructor(minimalInstructor);
                        } catch (Exception e) {
                            System.err.println("Error creating minimal instructor DTO for course " + course.getId() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    // Si le profil instructeur n'existe pas, créer un InstructorDto minimal
                    try {
                        com.odc.aws_learning.app.dto.InstructorDto minimalInstructor = new com.odc.aws_learning.app.dto.InstructorDto();
                        minimalInstructor.setId(course.getInstructor().getId());
                        minimalInstructor.setName(course.getInstructor().getFullName() != null ? 
                                course.getInstructor().getFullName() : 
                                course.getInstructor().getEmail() != null ? course.getInstructor().getEmail() : 
                                "Instructor #" + course.getInstructor().getId());
                        minimalInstructor.setAvatar(course.getInstructor().getAvatar());
                        dto.setInstructor(minimalInstructor);
                    } catch (Exception e2) {
                        System.err.println("Instructor profile not found for user " + course.getInstructor().getId() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            // Catch-all pour éviter que toute exception dans cette méthode ne bloque la requête
            System.err.println("Unexpected error in populateCalculatedFields for course " + (course != null ? course.getId() : "null") + ": " + e.getMessage());
            e.printStackTrace();
            // On continue avec les valeurs par défaut
        }
    }

    // NOUVEAU: Inscrit un utilisateur à un cours
    public CResponse<?> enrollUserInCourse(User user, Long courseId) {
        try {
            // Vérifier le nombre de cours en cours
            long activeCourses = detailsCourseRepo.countByLearnerIdAndCourseStatut(user.getId(), com.odc.aws_learning.app.constante.Enumeration.COURSE_STATUT.Learning);
            if (activeCourses >= 3) {
                return CResponse.error("Vous ne pouvez pas être inscrit à plus de 3 cours simultanément.");
            }

            // Vérifier que le cours existe
            Optional<Courses> courseOptional = coursesRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé avec l'ID: " + courseId);
            }

            Courses course = courseOptional.get();

            // Vérifier si l'utilisateur est déjà inscrit
            Optional<com.odc.aws_learning.app.entity.DetailsCourse> existingEnrollment = detailsCourseRepo
                    .findByCourseIdAndLearnerId(courseId, user.getId());

            if (existingEnrollment.isPresent()) {
                return CResponse.error("Vous êtes déjà inscrit à ce cours");
            }

            // Créer l'inscription
            com.odc.aws_learning.app.entity.DetailsCourse detailsCourse = new com.odc.aws_learning.app.entity.DetailsCourse();
            detailsCourse.setCourse(course);
            detailsCourse.setLearner(user);
            detailsCourse.setActivate(true);
            detailsCourseRepo.save(detailsCourse);

            // Envoyer une notification au formateur du cours
            if (course.getInstructor() != null) {
                try {
                    String learnerName = user.getFullName() != null ? user.getFullName() : user.getEmail();
                    String courseTitle = course.getTitle() != null ? course.getTitle() : "le cours";
                    String notificationMessage = learnerName + " s'est inscrit à votre cours: " + courseTitle;
                    String notificationLink = "/instructor/courses/" + course.getId();
                    
                    notificationService.createNotification(
                        course.getInstructor().getId(),
                        notificationMessage,
                        "enrollment",
                        notificationLink
                    );
                } catch (Exception e) {
                    // Ne pas faire échouer l'inscription si la notification échoue
                    System.err.println("Erreur lors de la création de la notification pour le formateur: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return CResponse.success(detailsCourse, "Inscription au cours réussie");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    /**
     * Désinscrire un utilisateur d'un cours
     */
    @Transactional
    public CResponse<?> unenrollUserFromCourse(Long userId, Long courseId) {
        try {
            // Vérifier que le cours existe
            Optional<Courses> courseOptional = coursesRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé avec l'ID: " + courseId);
            }

            // Vérifier si l'utilisateur est inscrit
            Optional<com.odc.aws_learning.app.entity.DetailsCourse> enrollmentOptional = detailsCourseRepo
                    .findByCourseIdAndLearnerId(courseId, userId);

            if (enrollmentOptional.isEmpty()) {
                return CResponse.error("L'utilisateur n'est pas inscrit à ce cours");
            }

            // Supprimer l'inscription
            detailsCourseRepo.delete(enrollmentOptional.get());

            return CResponse.success(null, "Désinscription du cours réussie");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la désinscription: " + e.getMessage());
        }
    }
}
