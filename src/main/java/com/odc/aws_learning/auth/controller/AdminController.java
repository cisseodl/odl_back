package com.odc.aws_learning.auth.controller;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Endpoint for creating an Admin (linking an existing User to an Admin entity)
    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE})
    @PreAuthorize("hasRole('ADMIN')") // Only existing Admins can create new Admins
    public CResponse<?> createAdmin(
            @RequestParam(value = "userId", required = false) Long userId,
            @org.springframework.web.bind.annotation.RequestBody(required = false) User user) {
        // Si userId est fourni en paramètre, l'utiliser
        if (userId != null) {
            User userToPromote = new User();
            userToPromote.setId(userId);
            return adminService.createAdmin(userToPromote);
        }
        // Sinon, utiliser l'objet User du body (pour compatibilité)
        if (user != null && user.getId() != null) {
        return adminService.createAdmin(user);
        }
        return CResponse.error("L'ID de l'utilisateur est requis (userId en paramètre ou id dans le body).");
    }

    // Endpoint for getting all Admins
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // Endpoint for getting a specific Admin by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }

    // Endpoint for updating an Admin (and potentially linked User details)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> updateAdmin(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody User userDetails) {
        return adminService.updateAdmin(id, userDetails);
    }

    // Endpoint for deleting an Admin (unlinking the Admin entity from the User)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }
}
