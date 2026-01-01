package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.CourseLevel;
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

    public CourseService(CoursesRepository coursesRepository, CourseMapper courseMapper, InstructorMapper instructorMapper, ReviewRepository reviewRepository, UserProgressRepository userProgressRepository, UserRepository userRepository, CategorieRepository categorieRepository, InstructorProfileRepository instructorProfileRepository, DetailsCourseRepo detailsCourseRepo, ModuleRepository moduleRepository, LessonRepository lessonRepository) {
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
    }

    public CourseDto getCourseById(Long id) {
        Courses course = coursesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id)); // Custom exception needed

        CourseDto courseDto = courseMapper.toDto(course);
        populateCalculatedFields(courseDto, course);
        return courseDto;
    }

    public List<CourseDto> getAllCourses(
            String category, CourseLevel level, String language, Boolean bestseller,
            int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Courses> coursesPage;
        // Basic filtering logic, can be expanded for more complex queries
        if (category != null && !category.isEmpty()) {
            // Find category by title (needs a method in CategorieRepository)
            Optional<Categorie> cat = this.categorieRepository.findByTitle(category);
            if (cat.isPresent()) {
                coursesPage = this.coursesRepository.findByCategorieId(cat.get().getId(), pageable);
            } else {
                coursesPage = Page.empty();
            }
        } else if (level != null) {
            coursesPage = this.coursesRepository.findByLevel(level, pageable);
        } else if (bestseller != null) {
            coursesPage = this.coursesRepository.findByBestseller(bestseller, pageable);
        } else {
            coursesPage = this.coursesRepository.findAll(pageable);
        }
        
        return coursesPage.getContent().stream()
                .map(course -> {
                    CourseDto dto = this.courseMapper.toDto(course);
                    populateCalculatedFields(dto, course);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public CourseDto createCourse(CourseCreationRequest request) {
        Courses course = new Courses();
        course.setTitle(request.getTitle());
        course.setSubtitle(request.getSubtitle());
        course.setDescription(request.getDescription());
        course.setImagePath(request.getImagePath());
        course.setLevel(request.getLevel());
        course.setLanguage(request.getLanguage());

        course.setObjectives(request.getObjectives());
        course.setFeatures(request.getFeatures());
        course.setCreatedAt(LocalDateTime.now()); // Set creation date

        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        course.setInstructor(instructor);

        Categorie category = categorieRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
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

    public void deleteCourse(Long id) {
        coursesRepository.deleteById(id);
    }

    private void populateCalculatedFields(CourseDto dto, Courses course) {
        // Calculate rating and review count
        Double avgRating = reviewRepository.findAverageRatingByCourse(course);
        Long reviewCount = reviewRepository.countByCourse(course);
        dto.setRating(avgRating != null ? avgRating : 0.0);
        dto.setReviewCount(reviewCount != null ? reviewCount.intValue() : 0);

        // Calculate enrolled count
        long enrolledCount = userProgressRepository.countDistinctUsersByCourse(course);
        dto.setEnrolledCount(enrolledCount);

        // Populate InstructorDto
        if (course.getInstructor() != null) {
            instructorProfileRepository.findByUser(course.getInstructor()).ifPresent(
                profile -> dto.setInstructor(instructorMapper.toDto(course.getInstructor(), profile))
            );
        }
    }

    // NOUVEAU: Inscrit un utilisateur à un cours
    public CResponse<?> enrollUserInCourse(User user, Long courseId) {
        try {
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

            return CResponse.success(detailsCourse, "Inscription au cours réussie");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de l'inscription: " + e.getMessage());
        }
    }
}
