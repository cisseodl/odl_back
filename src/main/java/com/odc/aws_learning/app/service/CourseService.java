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
import com.odc.aws_learning.app.repository.CourseEnrollmentExpectationsRepository;
import com.odc.aws_learning.app.service.CertificateService;
import com.odc.aws_learning.app.service.NotificationService;
import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.app.constante.CertificationMode;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

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
    private final CertificateService certificateService;
    private final CourseEnrollmentExpectationsRepository courseEnrollmentExpectationsRepository;
    private final NotificationService notificationService;
    private final SendEmailService sendEmailService;

    public CourseService(CoursesRepository coursesRepository, CourseMapper courseMapper, InstructorMapper instructorMapper, ReviewRepository reviewRepository, UserProgressRepository userProgressRepository, UserRepository userRepository, CategorieRepository categorieRepository, InstructorProfileRepository instructorProfileRepository, DetailsCourseRepo detailsCourseRepo, ModuleRepository moduleRepository, LessonRepository lessonRepository, CertificateRepository certificateRepository, CertificateService certificateService, CourseEnrollmentExpectationsRepository courseEnrollmentExpectationsRepository, NotificationService notificationService, SendEmailService sendEmailService) {
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
        this.certificateService = certificateService;
        this.courseEnrollmentExpectationsRepository = courseEnrollmentExpectationsRepository;
        this.notificationService = notificationService;
        this.sendEmailService = sendEmailService;
    }

    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id, User user) {
        try {
            // Charger le cours avec instructeur et catégorie (évite instructeur/avis/contenu manquants)
            Courses course = coursesRepository.findByIdWithRelations(id)
                    .orElseGet(() -> coursesRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id)));

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
                        try {
                            // Trier les modules par moduleOrder
                            modules.sort((m1, m2) -> {
                                if (m1 == null || m2 == null) return 0;
                                Integer order1 = m1.getModuleOrder() != null ? m1.getModuleOrder() : Integer.MAX_VALUE;
                                Integer order2 = m2.getModuleOrder() != null ? m2.getModuleOrder() : Integer.MAX_VALUE;
                                return order1.compareTo(order2);
                            });
                            // Trier les leçons de chaque module par lessonOrder
                            modules.forEach(module -> {
                                if (module != null && module.getLessons() != null && !module.getLessons().isEmpty()) {
                                    try {
                                        module.getLessons().sort((l1, l2) -> {
                                            if (l1 == null || l2 == null) return 0;
                                            Integer order1 = l1.getLessonOrder() != null ? l1.getLessonOrder() : Integer.MAX_VALUE;
                                            Integer order2 = l2.getLessonOrder() != null ? l2.getLessonOrder() : Integer.MAX_VALUE;
                                            return order1.compareTo(order2);
                                        });
                                    } catch (Exception e) {
                                        System.err.println("Error sorting lessons for module " + (module != null ? module.getId() : "null") + ": " + e.getMessage());
                                        // On continue même si le tri échoue
                                    }
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error sorting modules for course " + id + ": " + e.getMessage());
                            e.printStackTrace();
                            // On continue même si le tri échoue
                        }
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
                // Aperçu du contenu pour les non-inscrits : charger modules/leçons (titres uniquement, sans contentUrl)
                try {
                    List<Module> previewModules = moduleRepository.findAllByActivateAndCourseIdWithLessons(id);
                    if (previewModules != null && !previewModules.isEmpty()) {
                        previewModules.sort((m1, m2) -> {
                            if (m1 == null || m2 == null) return 0;
                            Integer o1 = m1.getModuleOrder() != null ? m1.getModuleOrder() : Integer.MAX_VALUE;
                            Integer o2 = m2.getModuleOrder() != null ? m2.getModuleOrder() : Integer.MAX_VALUE;
                            return o1.compareTo(o2);
                        });
                        previewModules.forEach(m -> {
                            if (m != null && m.getLessons() != null) {
                                m.getLessons().sort((l1, l2) -> {
                                    if (l1 == null || l2 == null) return 0;
                                    Integer o1 = l1.getLessonOrder() != null ? l1.getLessonOrder() : Integer.MAX_VALUE;
                                    Integer o2 = l2.getLessonOrder() != null ? l2.getLessonOrder() : Integer.MAX_VALUE;
                                    return o1.compareTo(o2);
                                });
                            }
                        });
                        course.setModules(previewModules);
                    } else {
                        course.setModules(new ArrayList<>());
                    }
                } catch (Exception e) {
                    System.err.println("Error loading preview modules for course " + id + ": " + e.getMessage());
                    course.setModules(new ArrayList<>());
                }
            }

            CourseDto courseDto = null;
            try {
                courseDto = courseMapper.toDto(course);
            } catch (Exception e) {
                System.err.println("Error mapping course to DTO for id " + id + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erreur lors du mapping du cours: " + e.getMessage(), e);
            }

            // Pour les non-inscrits : retirer les contentUrl (aperçu du contenu sans accès aux fichiers)
            if (!canAccessModules && courseDto != null && courseDto.getCurriculum() != null) {
                courseDto.getCurriculum().forEach(moduleDto -> {
                    if (moduleDto != null && moduleDto.getLessons() != null) {
                        moduleDto.getLessons().forEach(lessonDto -> {
                            if (lessonDto != null) lessonDto.setContentUrl(null);
                        });
                    }
                });
            }
            
            try {
                populateCalculatedFields(courseDto, course);
            } catch (Exception e) {
                System.err.println("Error populating calculated fields for course " + id + ": " + e.getMessage());
                e.printStackTrace();
                // On continue même si populateCalculatedFields échoue, le cours sera retourné avec des valeurs par défaut
            }
            
            return courseDto;
        } catch (RuntimeException e) {
            // Si c'est "Course not found", re-lancer l'exception
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw e;
            }
            // Pour les autres RuntimeException, essayer de récupérer un cours minimal
            System.err.println("RuntimeException in getCourseById for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            // Essayer de récupérer un cours minimal
            try {
                Courses course = coursesRepository.findById(id).orElse(null);
                if (course != null) {
                    course.setModules(new ArrayList<>());
                    CourseDto courseDto = courseMapper.toDto(course);
                    // Ne pas appeler populateCalculatedFields pour éviter d'autres exceptions
                    return courseDto;
                }
            } catch (Exception e2) {
                System.err.println("Error in fallback course retrieval: " + e2.getMessage());
                e2.printStackTrace();
            }
            // Si on ne peut pas récupérer le cours, re-lancer l'exception originale
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
                    // Ne pas appeler populateCalculatedFields pour éviter d'autres exceptions
                    return courseDto;
                }
            } catch (Exception e2) {
                System.err.println("Error in fallback course retrieval: " + e2.getMessage());
                e2.printStackTrace();
            }
            // Si on ne peut pas récupérer le cours, lancer une exception
            throw new RuntimeException("Erreur lors de la récupération du cours: " + e.getMessage(), e);
        }
    }

    public List<CourseDto> getAllCourses(
            String category, CourseLevel level, String language, Boolean bestseller, List<CourseStatus> statusList,
            int page, int size, String sortBy, String sortDir) {
        try {
            // Utiliser les méthodes avec FETCH pour charger les relations nécessaires
            List<Courses> coursesList;
            
            // Filtre par statut : liste optionnelle (Tous / Publié / Non publié)
            // Par défaut (listing public apprenant sans param) : uniquement les cours PUBLIE
            if (statusList != null && !statusList.isEmpty()) {
                if (statusList.size() == 1) {
                    coursesList = this.coursesRepository.findByStatusWithRelations(statusList.get(0));
                } else {
                    coursesList = this.coursesRepository.findByStatusInWithRelations(statusList);
                }
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
                // Listing public (Explorer les cours) : par défaut uniquement les cours publiés
                coursesList = this.coursesRepository.findByStatusWithRelations(CourseStatus.PUBLIE);
            }
            
            // Pour le listing public sans filtre, retourner plus de cours (éviter de limiter à 10)
            int effectiveSize = (statusList == null || statusList.isEmpty()) && category == null && level == null
                    ? Math.max(size, 100) : size;
            
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
            int start = page * effectiveSize;
            int end = Math.min(start + effectiveSize, sortedList.size());
            List<Courses> paginatedList = start < sortedList.size() ? 
                    sortedList.subList(start, end) : new ArrayList<>();
            
            // Mapper les cours en DTOs (résilient : ne pas perdre un cours si le mapping échoue)
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
                                // Ne pas perdre le cours : retourner un DTO minimal (sans curriculum)
                                return buildMinimalCourseDto(course);
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
    
    /** DTO minimal pour ne pas perdre un cours en liste quand le mapping complet échoue. */
    private CourseDto buildMinimalCourseDto(Courses course) {
        if (course == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle() != null ? course.getTitle() : "");
        dto.setSubtitle(course.getSubtitle());
        dto.setDescription(course.getDescription());
        dto.setImageUrl(course.getImagePath());
        dto.setCategory(course.getCategorie() != null && course.getCategorie().getTitle() != null
                ? course.getCategorie().getTitle() : "Non catégorisé");
        dto.setLevel(course.getLevel());
        dto.setStatus(course.getStatus());
        dto.setCurriculum(new ArrayList<>());
        dto.setEnrolledCount(0);
        return dto;
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
            // Cours dont l'instructeur est propriétaire OU cours sans formateur (pour qu'un instructeur puisse s'assigner en modifiant/sauvegardant)
            List<Courses> courses = coursesRepository.findByInstructor_IdOrInstructorNullWithRelations(instructorId);
            return courses.stream()
                    .map(course -> {
                        try {
                            CourseDto dto = courseMapper.toDto(course);

                            // FIX: Explicitly set the category title if the mapper didn't.
                            if (dto.getCategory() == null || dto.getCategory().isEmpty()) {
                                String categoryTitle = null;
                                if (course.getCategorie() != null) {
                                    categoryTitle = course.getCategorie().getTitle();
                                }
                                dto.setCategory(categoryTitle);
                            }
                            
                            populateCalculatedFields(dto, course);
                            return dto;
                        } catch (Exception e) {
                            System.err.println("Error processing course " + course.getId() + ": " + e.getMessage());
                            e.printStackTrace();
                            // Retourner un DTO minimal en cas d'erreur
                            try {
                                CourseDto dto = courseMapper.toDto(course);
                                return dto;
                            } catch (Exception e2) {
                                System.err.println("Error creating minimal DTO for course " + course.getId() + ": " + e2.getMessage());
                                e2.printStackTrace();
                                // Créer un DTO basique manuellement
                                CourseDto minimalDto = new CourseDto();
                                minimalDto.setId(course.getId());
                                minimalDto.setTitle(course.getTitle() != null ? course.getTitle() : "Sans titre");
                                minimalDto.setSubtitle(course.getSubtitle());
                                minimalDto.setDescription(course.getDescription());
                                minimalDto.setImageUrl(course.getImagePath());
                                minimalDto.setLevel(course.getLevel());
                                minimalDto.setLanguage(course.getLanguage());
                                minimalDto.setStatus(course.getStatus());
                                // Déterminer la catégorie directement
                                String categoryTitle = null;
                                if (course.getCategorie() != null) {
                                    categoryTitle = course.getCategorie().getTitle();
                                }
                                minimalDto.setCategory(categoryTitle);
                                return minimalDto;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getCoursesByInstructorId: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des cours de l'instructeur: " + e.getMessage(), e);
        }
    }

    @Transactional
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

            // Nouvelle hiérarchie : Catégorie -> Cours
            // Si categoryId est fourni, l'utiliser
            if (request.getCategoryId() != null) {
                Categorie category = categorieRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
                course.setCategorie(category);
            } else {
                throw new RuntimeException("CategoryId must be provided");
            }

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
        
        // Recharger le cours avec la catégorie pour s'assurer qu'elle est initialisée
        // Utiliser une requête qui charge explicitement la catégorie avec FETCH JOIN
        Courses courseWithCategory = coursesRepository.findByIdWithRelations(savedCourse.getId())
                .orElseThrow(() -> new RuntimeException("Course not found after creation"));
        
        return courseMapper.toDto(courseWithCategory); // Calculated fields will be null here
        } catch (RuntimeException e) {
            throw e; // Re-throw RuntimeException avec le message d'erreur
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cours: " + e.getMessage(), e);
        }
    }

    public CourseDto updateCourse(Long id, CourseUpdateRequest request, User currentUser) {
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
        existingCourse.setLastModifiedAt(LocalDateTime.now());

        // Seul le formateur (instructeur) propriétaire du cours peut le publier ; l'admin peut modifier le reste mais pas passer en PUBLIE
        boolean isAdmin = currentUser != null && currentUser.getAdmin() != null;
        boolean isInstructorOwner = existingCourse.getInstructor() != null && currentUser != null
                && existingCourse.getInstructor().getId().equals(currentUser.getId());
        if (request.getStatus() != null) {
            if (request.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.PUBLIE && isAdmin && !isInstructorOwner) {
                // Ne pas appliquer la publication si l'appelant est admin (seul le formateur publie)
            } else {
                existingCourse.setStatus(request.getStatus());
            }
        }
        if (request.getCertificationMode() != null) {
            existingCourse.setCertificationMode(request.getCertificationMode());
        }

        // Formateur : explicite dans la requête, ou conservé, ou assignation par l'instructeur connecté (cours sans formateur après import)
        if (request.getInstructorId() != null) {
            User instructor = userRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructeur non trouvé avec l'ID: " + request.getInstructorId()));
            existingCourse.setInstructor(instructor);
        } else if (existingCourse.getInstructor() == null && currentUser != null && currentUser.getInstructor() != null) {
            // Cours sans formateur (ex. après import / vidage DB) : l'instructeur qui enregistre s'assigne comme formateur
            existingCourse.setInstructor(currentUser);
        }
        // sinon on garde l'instructeur existant (existingCourse.getInstructor() inchangé)

        // Nouvelle hiérarchie : Catégorie -> Cours
        // Si categoryId est fourni, l'utiliser
        if (request.getCategoryId() != null) {
            Categorie category = categorieRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingCourse.setCategorie(category);
        }

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
        if (request == null || request.getAction() == null) {
            throw new IllegalArgumentException("L'action de validation (APPROVE, REJECT, WITHDRAW) est requise.");
        }
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        switch (request.getAction()) {
            case APPROVE:
                // isInstructorOwner = true uniquement pour l'instructeur propriétaire du cours (jamais pour l'admin).
                // Donc : seul l'instructeur peut passer le cours en PUBLIE ; l'admin ne fait que mettre en révision si besoin.
                if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.BROUILLON) {
                    if (isInstructorOwner) {
                        course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.PUBLIE);
                    } else {
                        // Admin ou autre : on ne publie pas, on met en révision ; l'instructeur publiera plus tard
                        course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.IN_REVIEW);
                    }
                    course.setRejectionReason(null);
                } else if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.IN_REVIEW) {
                    // Seul l'instructeur propriétaire peut publier (passer en PUBLIE)
                    if (!isInstructorOwner) {
                        throw new IllegalArgumentException("Seul le formateur (instructeur) assigné au cours peut le publier.");
                    }
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
            case WITHDRAW:
                // Rétirer un cours publié : PUBLIE -> BROUILLON (non publié), sans suppression
                if (course.getStatus() == com.odc.aws_learning.app.constante.CourseStatus.PUBLIE) {
                    course.setStatus(com.odc.aws_learning.app.constante.CourseStatus.BROUILLON);
                    course.setRejectionReason(null);
                } else {
                    throw new IllegalArgumentException("Seul un cours publié peut être retiré.");
                }
                break;
        }

        coursesRepository.save(course);
        // Recharger avec relations + modules pour éviter NPE/LazyInitializationException au mapping
        Courses reloaded = coursesRepository.findByIdWithRelationsAndModules(courseId)
                .orElseThrow(() -> new RuntimeException("Cours introuvable après mise à jour"));
        List<Module> modules = reloaded.getModules();
        if (modules == null) {
            reloaded.setModules(new ArrayList<>());
        } else {
            for (Module m : modules) {
                if (m != null) {
                    Hibernate.initialize(m.getLessons());
                }
            }
        }
        return courseMapper.toDto(reloaded);
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
            } else {
                // Cours sans instructeur assigné : envoyer un DTO par défaut pour que le front affiche toujours une ligne
                com.odc.aws_learning.app.dto.InstructorDto defaultInstructor = new com.odc.aws_learning.app.dto.InstructorDto();
                defaultInstructor.setId(null);
                defaultInstructor.setName("Instructeur non assigné");
                defaultInstructor.setTitle("Formateur");
                defaultInstructor.setAvatar(null);
                dto.setInstructor(defaultInstructor);
            }
        } catch (Exception e) {
            // Catch-all pour éviter que toute exception dans cette méthode ne bloque la requête
            System.err.println("Unexpected error in populateCalculatedFields for course " + (course != null ? course.getId() : "null") + ": " + e.getMessage());
            e.printStackTrace();
            // On continue avec les valeurs par défaut
        }
    }

    // NOUVEAU: Inscrit un utilisateur à un cours avec attentes
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class})
    public CResponse<?> enrollUserInCourse(User user, Long courseId, String expectations) {
        try {
            logger.info("=== DÉBUT INSCRIPTION AU COURS ===");
            logger.info("User ID: {}", user.getId());
            logger.info("Course ID: {}", courseId);
            logger.info("Expectations: {}", (expectations != null ? expectations.substring(0, Math.min(50, expectations.length())) : "null"));
            
            // Vérifier que les attentes sont fournies
            if (expectations == null || expectations.trim().isEmpty()) {
                logger.error("❌ Les attentes sont vides");
                return CResponse.error("Les attentes sont obligatoires pour s'inscrire au cours");
            }

            // Vérifier le nombre de cours en cours
            long activeCourses = detailsCourseRepo.countByLearnerIdAndCourseStatut(user.getId(), com.odc.aws_learning.app.constante.Enumeration.COURSE_STATUT.Learning);
            logger.info("Nombre de cours actifs: {}", activeCourses);
            if (activeCourses >= 3) {
                logger.error("❌ Trop de cours actifs: {}", activeCourses);
                return CResponse.error("Vous devez terminer l'un de vos cours en cours avant de vous inscrire à un autre.");
            }

            // Vérifier que le cours existe
            Optional<Courses> courseOptional = coursesRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                logger.error("❌ Cours non trouvé: {}", courseId);
                return CResponse.error("Cours non trouvé avec l'ID: " + courseId);
            }

            Courses course = courseOptional.get();
            logger.info("✅ Cours trouvé: {}", course.getTitle());

            // Vérifier si l'utilisateur est déjà inscrit
            Optional<com.odc.aws_learning.app.entity.DetailsCourse> existingEnrollment = detailsCourseRepo
                    .findByCourseIdAndLearnerId(courseId, user.getId());

            if (existingEnrollment.isPresent()) {
                logger.error("❌ Utilisateur déjà inscrit");
                return CResponse.error("Vous êtes déjà inscrit à ce cours");
            }

            // Créer l'inscription
            logger.info("Création de DetailsCourse...");
            com.odc.aws_learning.app.entity.DetailsCourse detailsCourse = new com.odc.aws_learning.app.entity.DetailsCourse();
            detailsCourse.setCourse(course);
            detailsCourse.setLearner(user);
            detailsCourse.setActivate(true);
            detailsCourse.setCourseStatut(com.odc.aws_learning.app.constante.Enumeration.COURSE_STATUT.Learning);
            
            // Sauvegarder l'inscription
            logger.info("Sauvegarde de DetailsCourse...");
            detailsCourse = detailsCourseRepo.save(detailsCourse);
            logger.info("DetailsCourse sauvegardé avec ID: {}", detailsCourse.getId());
            
            // S'assurer que l'inscription est bien flushée avant de créer les attentes
            detailsCourseRepo.flush();
            logger.info("✅ DetailsCourse flushé, ID: {}", detailsCourse.getId());
            
            // Vérifier que l'ID est bien généré
            if (detailsCourse.getId() == null) {
                logger.error("❌ ERREUR CRITIQUE: DetailsCourse ID est null après flush!");
                throw new RuntimeException("L'ID de DetailsCourse n'a pas été généré après la sauvegarde");
            }

            // Enregistrer les attentes dans la même transaction
            logger.info("Création de CourseEnrollmentExpectations...");
            com.odc.aws_learning.app.entity.CourseEnrollmentExpectations expectationsEntity = 
                new com.odc.aws_learning.app.entity.CourseEnrollmentExpectations(detailsCourse, expectations);
            logger.info("Sauvegarde de CourseEnrollmentExpectations avec detailsCourseId: {}", detailsCourse.getId());
            expectationsEntity = courseEnrollmentExpectationsRepository.save(expectationsEntity);
            courseEnrollmentExpectationsRepository.flush();
            logger.info("✅ CourseEnrollmentExpectations sauvegardé avec ID: {}", expectationsEntity.getId());

            // Retourner le succès AVANT d'envoyer la notification pour éviter que l'erreur de notification
            // ne marque la transaction comme rollback-only
            CResponse<?> successResponse = CResponse.success(detailsCourse, "Inscription au cours réussie");
            logger.info("✅ Réponse de succès créée");
            
            logger.info("=== FIN INSCRIPTION AU COURS (SUCCÈS) ===");
            
            // IMPORTANT: Retourner la réponse AVANT d'envoyer la notification
            // Cela garantit que la transaction principale est commitée avant l'envoi de la notification
            // Envoyer une notification au formateur du cours dans une transaction séparée (après le commit)
            // Cela évite que l'erreur de notification ne fasse échouer l'inscription
            if (course.getInstructor() != null) {
                logger.info("Envoi de notification asynchrone au formateur ID: {}", course.getInstructor().getId());
                // Appeler de manière asynchrone pour ne pas bloquer la transaction
                // Utiliser un try-catch séparé pour isoler complètement l'erreur
                try {
                    sendNotificationAsync(course.getInstructor().getId(), user, course);
                } catch (Exception notificationError) {
                    // Ne pas faire échouer l'inscription si la notification échoue
                    // Cette erreur ne doit PAS affecter la transaction principale
                    logger.warn("⚠️ Erreur lors de la création de la notification pour le formateur (non bloquante): {}", notificationError.getMessage(), notificationError);
                }
            } else {
                logger.warn("⚠️ Aucun formateur associé au cours");
            }
            
            return successResponse;
        } catch (Exception e) {
            logger.error("❌❌❌ ERREUR LORS DE L'INSCRIPTION AU COURS ❌❌❌", e);
            logger.error("Type d'erreur: {}", e.getClass().getName());
            logger.error("Message: {}", e.getMessage());
            logger.error("Cause: {}", (e.getCause() != null ? e.getCause().getMessage() : "N/A"));
            return CResponse.error("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    /**
     * Envoyer une notification de manière asynchrone dans une transaction séparée
     * pour éviter que l'erreur de notification ne fasse échouer l'inscription
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {Exception.class})
    private void sendNotificationAsync(Long instructorId, User learner, Courses course) {
        try {
            logger.info("[sendNotificationAsync] Début de l'envoi de notification pour instructeur ID: {}", instructorId);
            String learnerName = learner.getFullName() != null ? learner.getFullName() : learner.getEmail();
            String courseTitle = course.getTitle() != null ? course.getTitle() : "le cours";
            String notificationMessage = learnerName + " s'est inscrit à votre cours: " + courseTitle;
            String notificationLink = "/instructor/courses/" + course.getId();
            
            logger.info("[sendNotificationAsync] Création de la notification...");
            CResponse<?> notificationResponse = notificationService.createNotification(
                instructorId,
                notificationMessage,
                "enrollment",
                notificationLink
            );
            
            if (notificationResponse != null && notificationResponse.isSuccess()) {
                logger.info("[sendNotificationAsync] ✅ Notification créée avec succès");
            } else {
                logger.warn("[sendNotificationAsync] ⚠️ Notification créée mais réponse indique un échec: {}", 
                    (notificationResponse != null ? notificationResponse.getMessage() : "null"));
            }
        } catch (Exception e) {
            // Log l'erreur mais ne pas la propager pour ne pas affecter la transaction principale
            // L'annotation noRollbackFor garantit que cette exception ne marquera pas la transaction comme rollback-only
            logger.error("[sendNotificationAsync] ❌ Erreur lors de la création de la notification pour le formateur (asynchrone): {}", e.getMessage(), e);
            logger.error("[sendNotificationAsync] Type d'erreur: {}", e.getClass().getName());
            // Ne pas re-lancer l'exception pour éviter qu'elle affecte la transaction principale
        }
    }

    /**
     * Récupérer les attentes d'inscription d'un utilisateur pour un cours
     * @param userId ID de l'utilisateur
     * @param courseId ID du cours
     * @return CResponse contenant les attentes ou une erreur
     */
    @Transactional(readOnly = true)
    public CResponse<?> getEnrollmentExpectations(Long userId, Long courseId) {
        try {
            // Vérifier que le cours existe
            Optional<Courses> courseOptional = coursesRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé avec l'ID: " + courseId);
            }

            // Vérifier que l'utilisateur existe
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
            }

            // Récupérer l'inscription
            Optional<DetailsCourse> enrollmentOptional = detailsCourseRepo.findByCourseIdAndLearnerId(courseId, userId);
            if (enrollmentOptional.isEmpty()) {
                return CResponse.error("L'utilisateur n'est pas inscrit à ce cours");
            }

            DetailsCourse enrollment = enrollmentOptional.get();

            // Récupérer les attentes
            Optional<com.odc.aws_learning.app.entity.CourseEnrollmentExpectations> expectationsOptional = 
                courseEnrollmentExpectationsRepository.findByDetailsCourseId(enrollment.getId());

            if (expectationsOptional.isEmpty()) {
                return CResponse.error("Aucune attente trouvée pour cette inscription");
            }

            com.odc.aws_learning.app.entity.CourseEnrollmentExpectations expectations = expectationsOptional.get();
            
            // Créer un DTO pour la réponse
            java.util.Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("id", expectations.getId());
            responseData.put("expectations", expectations.getExpectations());
            responseData.put("courseId", courseId);
            responseData.put("userId", userId);
            responseData.put("enrollmentId", enrollment.getId());
            responseData.put("createdAt", expectations.getCreatedAt());
            responseData.put("lastModifiedAt", expectations.getLastModifiedAt());

            return CResponse.success(responseData, "Attentes d'inscription récupérées avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la récupération des attentes: " + e.getMessage());
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

    /**
     * Attribue le certificat à un apprenant après validation des labs par l'instructeur (mode BY_LABS).
     * L'instructeur du cours (ou un admin) peut appeler cette méthode. Génère le certificat et envoie l'email de félicitations.
     */
    @Transactional
    public CResponse<?> approveCertificateByLabs(Long courseId, Long learnerId, User instructorOrAdmin) {
        try {
            Optional<Courses> courseOpt = coursesRepository.findById(courseId);
            if (courseOpt.isEmpty()) {
                return CResponse.error("Cours non trouvé");
            }
            Courses course = courseOpt.get();
            boolean isAdmin = instructorOrAdmin.getAdmin() != null;
            boolean isInstructorOfCourse = course.getInstructor() != null && course.getInstructor().getId().equals(instructorOrAdmin.getId());
            if (!isAdmin && !isInstructorOfCourse) {
                return CResponse.error("Vous n'êtes pas autorisé à attribuer le certificat pour ce cours");
            }
            Optional<User> learnerOpt = userRepository.findById(learnerId);
            if (learnerOpt.isEmpty()) {
                return CResponse.error("Apprenant non trouvé");
            }
            User learner = learnerOpt.get();
            Optional<DetailsCourse> enrollmentOpt = detailsCourseRepo.findByCourseIdAndLearnerId(courseId, learnerId);
            if (enrollmentOpt.isEmpty()) {
                return CResponse.error("L'apprenant n'est pas inscrit à ce cours");
            }
            if (certificateRepository.existsByUser_IdAndCourse_Id(learnerId, courseId)) {
                return CResponse.error("Cet apprenant possède déjà un certificat pour ce cours");
            }
            CResponse<com.odc.aws_learning.app.entity.Certificate> certResponse = certificateService.generateCertificateForLabsCompletion(learner, course);
            if (certResponse.isFailed()) {
                return CResponse.error(certResponse.getMessage());
            }
            try {
                sendEmailService.sendCertificateCongratulationsByLabs(
                    learner.getEmail(),
                    learner.getFullName() != null ? learner.getFullName() : learner.getEmail(),
                    course.getTitle()
                );
            } catch (Exception e) {
                // Ne pas faire échouer la requête si l'email échoue
                org.slf4j.LoggerFactory.getLogger(CourseService.class).warn("Envoi email félicitations certificat échoué: {}", e.getMessage());
            }
            return CResponse.success(certResponse.getData(), "Certificat attribué avec succès. Un email de félicitations a été envoyé à l'apprenant.");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de l'attribution du certificat: " + e.getMessage());
        }
    }
}
