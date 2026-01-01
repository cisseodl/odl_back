package com.odc.aws_learning.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.constante.CourseLevel; // Added
import com.odc.aws_learning.app.dto.CourseCreationRequest;
import com.odc.aws_learning.app.dto.CourseDto;
import com.odc.aws_learning.app.dto.CourseUpdateRequest; // Added
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.service.CourseService;
import com.odc.aws_learning.app.service.S3Service; // Added
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

@RequestMapping("/courses")
@RestController
@RequiredArgsConstructor
public class CoursesController {
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final S3Service s3Service; // Injected
    private final ObjectMapper objectMapper; // Injected


    @PostMapping("/save/{catId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
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
            String imageUrl = s3Service.saveFile(image, "courses");
            request.setImagePath(imageUrl);
        }

        request.setInstructorId(currentUser.getId());
        request.setCategoryId(catId);

        CourseDto createdCourse = courseService.createCourse(request);
        return CResponse.success(createdCourse, "Cours enregistré avec succès");
    }

     @GetMapping("/read/{id}")
    public CResponse<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto courseDto = courseService.getCourseById(id);
        return CResponse.success(courseDto);
    }


       @PutMapping("/{id}")
       @PreAuthorize("hasRole('INSTRUCTOR')")
       public CResponse<CourseDto> updateCourse(@PathVariable Long id,
                                                  @RequestParam("courses") String coursestring,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {
           try {
               CourseUpdateRequest request = objectMapper.readValue(coursestring, CourseUpdateRequest.class);
               if (image != null && !image.isEmpty()) {
                   String imageUrl = s3Service.saveFile(image, "courses");
                   request.setImagePath(imageUrl);
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

    // Récupérer tous les cours
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<List<CourseDto>> getAllCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean bestseller,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        List<CourseDto> courses = courseService.getAllCourses(category, level, language, bestseller, page, size, sortBy, sortDir);
        return CResponse.success(courses, "Liste des cours récupérée avec succès");
    }



    /**
     * S'inscrire à un cours (inscription gratuite directe).
     * POST /courses/enroll/{courseId}
     * Accessible à tous les utilisateurs authentifiés (USER, LEARNER, ADMIN)
     */
    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public ResponseEntity<CResponse<?>> enrollInCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.ok(CResponse.error("Utilisateur non authentifié"));
        }
        
        CResponse<?> response = courseService.enrollUserInCourse(currentUser, courseId);
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
