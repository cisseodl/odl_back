package com.odc.aws_learning.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.service.CoursesService;
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
    private final CoursesService coursesService;
    private final UserRepository userRepository;


    @PostMapping("/save/{catId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> addCourseWithImage(
            @PathVariable Long catId,
            @RequestParam("courses") String coursestring,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Courses courses = new ObjectMapper().readValue(coursestring, Courses.class);
//        System.out.println("Bonjour!");
        return coursesService.addCourseWithImage(courses, image, catId);
    }

     @GetMapping("/read/{id}")
    public CResponse<?> getCourseById(@PathVariable Long id) {
        return coursesService.getCourseById(id);
    }


       @PutMapping("/update")
       @PreAuthorize("hasRole('ADMIN')")
       public ResponseEntity<String> updateCourse(
                                                  @RequestParam("courses") String coursestring,
                                                  @RequestParam("image") MultipartFile image) {
           try {
               Courses courses = new ObjectMapper().readValue(coursestring, Courses.class);
               coursesService.updateCourse(courses, image);
               return ResponseEntity.ok().body("Le cours a été mis à jour avec succès.");
           } catch (IOException e) {
               System.out.println(e);
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la modification.");
           }
       }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        if (coursesService.deleteCourse(id)) {
            return ResponseEntity.ok().body("Le cours a été supprimé avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer tous les cours
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public List<Courses> getAllCourses() {
        return coursesService.getAllCourses();
    }

    @GetMapping("/page/{page}/{size}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getCoursesByPage(@PathVariable int page, @PathVariable int size)  {
        return coursesService.getCoursesByPage(page, size);
    }
    // Récupérer tous les cours par categorie
    @GetMapping("/read/by-category/{catId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public List<Courses> getCoursesByCategory(@PathVariable Long catId) {
        return coursesService.getCoursesByCategory(catId);
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
        
        CResponse<?> response = coursesService.enrollUserInCourse(currentUser, courseId);
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
