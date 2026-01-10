package com.odc.aws_learning.auth.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.dao.request.InstructorRequest;
import com.odc.aws_learning.auth.dao.request.InstructorUpdateRequest; // Ajouté
import com.odc.aws_learning.auth.entities.User; // Keep for other methods if needed
import com.odc.aws_learning.auth.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // Keep for other methods
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.security.Principal; // Added for Principal

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    // Endpoint for creating an Instructor (Authenticated User creates their own Instructor profile or Admin creates for another user)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()") // Only authenticated users can create their Instructor profile
    public CResponse<?> createInstructor(
            Principal principal, // To get authenticated user's details
            @org.springframework.web.bind.annotation.RequestBody InstructorRequest request // Utilisation du DTO
            ) {
        // Si userId est fourni, un admin crée un profil instructor pour un autre utilisateur
        if (request.getUserId() != null) {
            return instructorService.createInstructorForUser(
                request.getUserId(),
                request.getBiography(),
                request.getSpecialization()
            );
        }
        // Sinon, utiliser l'email fourni ou celui de l'utilisateur connecté
        String userEmail = request.getUserEmail() != null ? request.getUserEmail() : principal.getName();
        return instructorService.createInstructorAuthenticated(
            userEmail, // User's email from request or JWT
            request.getBiography(), // Extraire de la requête DTO
            request.getSpecialization() // Extraire de la requête DTO
        );
    }

    // Endpoint for getting all Instructors
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')") // Admins and Instructors can view all instructors
    public CResponse<?> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    // Endpoint for getting a specific Instructor by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> getInstructorById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    // Endpoint for updating an Instructor (and potentially linked User details)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // Ajouté consumes
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')") // Admins can update any, Instructor can update self
    public CResponse<?> updateInstructor(@PathVariable Long id,
                                          @org.springframework.web.bind.annotation.RequestBody InstructorUpdateRequest request) { // Utilisation du DTO
        // Additional logic might be needed here to ensure an instructor can only update their own profile
        return instructorService.updateInstructor(id, request);
    }

    // Endpoint for deleting an Instructor (unlinking the Instructor entity from the User)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can delete Instructors
    public CResponse<?> deleteInstructor(@PathVariable Long id) {
        return instructorService.deleteInstructor(id);
    }
}
