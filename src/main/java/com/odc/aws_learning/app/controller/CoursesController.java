package com.odc.aws_learning.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.constante.CourseLevel; // Added
import com.odc.aws_learning.app.constante.CourseStatus; // Added
import com.odc.aws_learning.app.dto.CourseCreationRequest;
import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.dto.CourseUpdateRequest; // Added
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.service.CourseService;
import com.odc.aws_learning.app.service.UploadFileService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;

@Slf4j
@RequestMapping("/courses")
@RestController
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com"}, maxAge = 3600)
@RequiredArgsConstructor
public class CoursesController {
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final com.odc.aws_learning.app.repository.CoursesRepository coursesRepository;
    private final UploadFileService uploadFileService;
    private final ObjectMapper objectMapper; // Injected
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.server.base-url:https://api.smart-odc.com}")
    private String serverBaseUrl;


    /**
     * Crée un nouveau cours avec une image optionnelle
     * @param catId ID de la catégorie
     * @param coursestring JSON stringifié du CourseCreationRequest
     * @param image Image optionnelle du cours
     * @return CResponse contenant le CourseDto créé
     * @throws IOException Si l'upload de l'image échoue
     */
    @PostMapping(value = "/save/{catId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> addCourseWithImage(
            @PathVariable Long catId,
            @RequestPart("courses") @Valid CourseCreationRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (image != null && !image.isEmpty()) {
            try {
                // Utiliser le stockage local (Elastic Beanstalk)
                String localFolderPath = uploadDir + "/courses";
                String savedFileName = uploadFileService.uploadFile(image, localFolderPath);
                String imageUrl = serverBaseUrl + "/awsodclearning/api/files/courses/" + savedFileName;
                log.info("Image du cours sauvegardée localement: {}", imageUrl);
                request.setImagePath(imageUrl);
            } catch (IOException ioException) {
                log.error("Erreur lors de la sauvegarde locale de l'image du cours: {}", ioException.getMessage(), ioException);
                // Continuer sans image si l'upload échoue
            }
        }

        // Si l'utilisateur est un instructeur, définir son ID (par rôle pour éviter LazyInitializationException).
        // Si c'est un admin, l'ID de l'instructeur doit être dans la requête.
        boolean isInstructor = currentUser.getInstructor() != null
                || (authentication != null && authentication.getAuthorities().stream()
                        .anyMatch(a -> "ROLE_INSTRUCTOR".equals(a.getAuthority())));
        boolean isAdmin = currentUser.getAdmin() != null
                || (authentication != null && authentication.getAuthorities().stream()
                        .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));
        if (isInstructor) {
            request.setInstructorId(currentUser.getId());
        } else if (isAdmin) {
            if (request.getInstructorId() == null) {
                return CResponse.error("L'ID de l'instructeur est requis pour les administrateurs.");
            }
            // L'ID de l'instructeur est déjà dans la requête.
        } else {
            return CResponse.error("L'utilisateur n'est ni instructeur ni admin.");
        }
        
        request.setCategoryId(catId);

        try {
            CourseDto createdCourse = courseService.createCourse(request);
            return CResponse.success(createdCourse, "Cours enregistré avec succès");
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création du cours: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la création du cours: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la création du cours: {}", e.getMessage(), e);
            return CResponse.error("Erreur inattendue lors de la création du cours: " + e.getMessage());
        }
    }

     @GetMapping("/read/{id}")
    // Endpoint authentifié : seuls les utilisateurs authentifiés peuvent voir les modules et leçons
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                log.warn("getCurrentUser() returned null for authenticated endpoint /courses/read/{}", id);
                // Même si l'utilisateur est null, on peut quand même essayer de récupérer le cours
                // mais sans les modules (car l'utilisateur n'est pas authentifié)
            }
            
            log.debug("Fetching course {} for user {}", id, currentUser != null ? currentUser.getId() : "null");
            CourseDto courseDto = courseService.getCourseById(id, currentUser);
            
            if (courseDto == null) {
                log.warn("CourseDto is null for course id {}", id);
                return CResponse.error("Cours non trouvé avec l'ID: " + id);
            }
            
            return CResponse.success(courseDto);
        } catch (RuntimeException e) {
            log.error("RuntimeException getting course by id {}: {}", id, e.getMessage(), e);
            // Si c'est "Course not found", retourner une erreur appropriée
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return CResponse.error("Cours non trouvé avec l'ID: " + id);
            }
            // Sinon, retourner une erreur générique avec plus de détails en mode debug
            String errorMessage = "Erreur lors de la récupération du cours: " + e.getMessage();
            log.error("Full exception stack trace:", e);
            return CResponse.error(errorMessage);
        } catch (Exception e) {
            log.error("Unexpected error getting course by id {}: {}", id, e.getMessage(), e);
            log.error("Full exception stack trace:", e);
            return CResponse.error("Erreur inattendue lors de la récupération du cours: " + e.getMessage());
        }
    }


       @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
       @PreAuthorize("hasRole('INSTRUCTOR')")
       public CResponse<CourseDto> updateCourse(@PathVariable Long id,
                                                  @RequestParam("courses") String coursestring,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {
           try {
               CourseUpdateRequest request = objectMapper.readValue(coursestring, CourseUpdateRequest.class);
               if (image != null && !image.isEmpty()) {
                   try {
                       // Utiliser le stockage local (Elastic Beanstalk)
                       String localFolderPath = uploadDir + "/courses";
                       String savedFileName = uploadFileService.uploadFile(image, localFolderPath);
                       String imageUrl = serverBaseUrl + "/awsodclearning/api/files/courses/" + savedFileName;
                       log.info("Image du cours mise à jour localement: {}", imageUrl);
                       request.setImagePath(imageUrl);
                   } catch (IOException ioException) {
                       log.error("Erreur lors de la sauvegarde locale de l'image du cours: {}", ioException.getMessage(), ioException);
                       // Continuer sans mettre à jour l'image si l'upload échoue
                   }
               }
               CourseDto updatedCourse = courseService.updateCourse(id, request);
               return CResponse.success(updatedCourse, "Le cours a été mis à jour avec succès.");
           } catch (IOException e) {
               System.out.println(e);
               return CResponse.error("Une erreur s'est produite lors de la modification: " + e.getMessage());
           }
       }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return CResponse.success("Le cours a été supprimé avec succès.");
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<CourseDto> validateCourse(@PathVariable Long id, @RequestBody com.odc.aws_learning.app.dto.CourseValidationRequest request) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return CResponse.error("Utilisateur non authentifié");
            }
            
            // Vérifier que l'instructeur ne peut valider que ses propres cours
            Optional<Courses> courseOptional = coursesRepository.findById(id);
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé");
            }
            Courses course = courseOptional.get();
            
            // Si l'utilisateur est un instructeur (pas admin), vérifier qu'il est propriétaire du cours
            boolean isInstructor = currentUser.getAdmin() == null && currentUser.getInstructor() != null;
            boolean isOwner = course.getInstructor() != null && course.getInstructor().getId().equals(currentUser.getId());
            
            if (isInstructor && !isOwner) {
                return CResponse.error("Vous n'êtes pas autorisé à modifier ce cours.");
            }
            
            // Passer l'information si c'est un instructeur propriétaire pour permettre la validation directe
            CourseDto updatedCourse = courseService.validateCourse(id, request, isInstructor && isOwner);
            return CResponse.success(updatedCourse, "Le statut du cours a été mis à jour.");
        } catch (IllegalArgumentException e) {
            return CResponse.error(e.getMessage());
        } catch (RuntimeException e) {
            return CResponse.error(e.getMessage());
        }
    }

    // Récupérer tous les cours
    @GetMapping("/read")
    // Endpoint public pour permettre aux apprenants de consulter les cours sans authentification
    // Par défaut, seuls les cours publiés sont retournés pour le frontend apprenant
    public CResponse<List<CourseDto>> getAllCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean bestseller,
            @RequestParam(required = false) List<CourseStatus> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        // Si aucun statut n'est spécifié, filtrer par défaut sur PUBLIE pour le frontend apprenant
        CourseStatus finalStatus = status != null ? status : com.odc.aws_learning.app.constante.CourseStatus.PUBLIE;
        List<CourseDto> courses = courseService.getAllCourses(category, level, language, bestseller, finalStatus, page, size, sortBy, sortDir);
        return CResponse.success(courses, "Liste des cours récupérée avec succès");
    }

    // Récupérer les cours d'un instructeur
    @GetMapping("/read/by-instructor/{instructorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<List<CourseDto>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseDto> courses = courseService.getCoursesByInstructorId(instructorId);
        return CResponse.success(courses, "Liste des cours de l'instructeur récupérée avec succès");
    }



    /**
     * S'inscrire à un cours avec attentes obligatoires.
     * POST /courses/enroll/{courseId}
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN)
     */
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public ResponseEntity<CResponse<?>> enrollInCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody(required = false) com.odc.aws_learning.app.dto.EnrollmentRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        String expectations = (request != null && request.getExpectations() != null) 
            ? request.getExpectations() 
            : "";
        
        CResponse<?> response = courseService.enrollUserInCourse(currentUser, courseId, expectations);
        return ResponseEntity.ok(response);
    }

    /**
     * Désinscrire un utilisateur d'un cours.
     * DELETE /courses/unenroll/{courseId}/user/{userId}
     * Accessible aux admins et instructeurs
     */
    @DeleteMapping("/unenroll/{courseId}/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<?>> unenrollUserFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        CResponse<?> response = courseService.unenrollUserFromCourse(userId, courseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère l'utilisateur actuellement authentifié
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.orElse(null);
        }
        return null;
    }

}
