package com.odc.aws_learning.auth.controller;

import com.odc.aws_learning.auth.service.UserService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all/{page}/{size}")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> getAll(@PathVariable int page, @PathVariable int size) {
        return userService.getAll(page, size);
    }

    @GetMapping("/check/{phone}")
    public CResponse<?> checkUserByPhone(@PathVariable String phone) {
        return userService.checkUserByPhone(phone);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'INSTRUCTOR', 'APPRENANT')") // Updated roles
    public CResponse<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Supprime un utilisateur et toutes ses entités liées en cascade (Admin, Instructor, Apprenant, etc.)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Seuls les admins peuvent supprimer des utilisateurs
    public CResponse<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
