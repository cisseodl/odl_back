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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;

@Slf4j
@RequestMapping("/courses")
@RestController
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


    @PostMapping(value = "/save/{catId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> addCourseWithImage(
            @PathVariable Long catId,
            @RequestParam("courses") String coursestring,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }

        CourseCreationRequest request = objectMapper.readValue(coursestring, CourseCreationRequest.class);

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

        // Si l'utilisateur est ADMIN et qu'un instructorId est fourni dans le payload, l'utiliser
        // Sinon, utiliser l'ID de l'utilisateur connecté (pour les instructeurs qui créent leurs propres cours)
        if (currentUser.getAdmin() != null && request.getInstructorId() != null) {
            // Admin peut créer un cours pour un autre instructeur
            // L'instructorId du payload est déjà défini, on le garde
        } else {
            // Pour les instructeurs, utiliser leur propre ID
            request.setInstructorId(currentUser.getId());
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
    public CResponse<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto courseDto = courseService.getCourseById(id);
        return CResponse.success(courseDto);
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
            if (currentUser.getAdmin() == null 
                && course.getInstructor() != null 
                && !course.getInstructor().getId().equals(currentUser.getId())) {
                return CResponse.error("Vous n'êtes pas autorisé à modifier ce cours.");
            }
            
            CourseDto updatedCourse = courseService.validateCourse(id, request);
            return CResponse.success(updatedCourse, "Le statut du cours a été mis à jour.");
        } catch (IllegalArgumentException e) {
            return CResponse.error(e.getMessage());
        } catch (RuntimeException e) {
            return CResponse.error(e.getMessage());
        }
    }

    // Récupérer tous les cours
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<List<CourseDto>> getAllCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean bestseller,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        List<CourseDto> courses = courseService.getAllCourses(category, level, language, bestseller, status, page, size, sortBy, sortDir);
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
     * S'inscrire à un cours (inscription gratuite directe).
     * POST /courses/enroll/{courseId}
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN)
     */
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public ResponseEntity<CResponse<?>> enrollInCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        CResponse<?> response = courseService.enrollUserInCourse(currentUser, courseId);
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
