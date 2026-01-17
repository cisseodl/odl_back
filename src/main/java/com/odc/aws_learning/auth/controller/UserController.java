package com.odc.aws_learning.auth.controller;

import com.odc.aws_learning.auth.service.UserService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all/{page}/{size}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<?>> getAll(@PathVariable int page, @PathVariable int size) {
        try {
            CResponse<?> response = userService.getAll(page, size);
            // Retourner toujours un ResponseEntity avec le code HTTP approprié
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                // Même en cas d'erreur, retourner 200 avec ok=false pour que le frontend puisse gérer
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // En cas d'exception non gérée, retourner une erreur structurée
            System.err.println("Erreur dans UserController.getAll(): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(CResponse.error("Erreur lors de la récupération des utilisateurs: " + e.getMessage()));
        }
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
     * Blacklister (désactiver) un utilisateur
     */
    @PutMapping("/{id}/blacklist")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> blacklistUser(@PathVariable Long id) {
        return userService.blacklistUser(id);
    }

    /**
     * Déblacklister (réactiver) un utilisateur
     */
    @PutMapping("/{id}/unblacklist")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> unblacklistUser(@PathVariable Long id) {
        return userService.unblacklistUser(id);
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
