package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.dto.AdminWithUserDto;
import com.odc.aws_learning.auth.entities.Admin;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.AdminRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository; // To manage User entity
    private final SendEmailService sendEmailService;
    private final com.odc.aws_learning.app.service.AuditService auditService;
    private final com.odc.aws_learning.app.service.NotificationService notificationService;
    
    @Value("${app.frontend.url:https://admin.smart-odc.com}")
    private String frontendUrl;

    @Transactional
    public CResponse<?> createAdmin(User user) {
        if (user.getId() == null) {
            return CResponse.error("L'ID de l'utilisateur est requis.");
        }
        
        // Charger l'utilisateur complet depuis la base de données
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isEmpty()) {
            return CResponse.error("L'utilisateur doit exister avant de créer un Admin.");
        }
        
        User existingUser = userOptional.get();
        
        // Vérifier si l'utilisateur est déjà un administrateur
        if (adminRepository.findByUserId(existingUser.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un administrateur.");
        }

        // Créer l'admin avec l'utilisateur chargé depuis la base
        Admin admin = new Admin(existingUser);
        Admin savedAdmin = adminRepository.save(admin);
        
        // Link admin to user for bidirectional consistency
        existingUser.setAdmin(savedAdmin);
        userRepository.save(existingUser);

        // Créer un log d'activité
        try {
            auditService.logActivity(existingUser.getId(), "create", "admin", 
                "{\"adminId\":" + savedAdmin.getId() + ",\"userName\":\"" + 
                (existingUser.getFullName() != null ? existingUser.getFullName() : existingUser.getEmail()) + "\"}");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du log d'activité: " + e.getMessage());
        }

        // Créer une notification pour les autres admins
        try {
            List<Admin> allAdmins = adminRepository.findAll();
            for (Admin otherAdmin : allAdmins) {
                User adminUser = otherAdmin.getUser();
                if (adminUser != null && !adminUser.getId().equals(existingUser.getId())) {
                    notificationService.createNotification(
                        adminUser.getId(),
                        "Nouvel administrateur créé: " + (existingUser.getFullName() != null ? existingUser.getFullName() : existingUser.getEmail()),
                        "registration",
                        "/admin/users/administrateurs"
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la notification: " + e.getMessage());
        }

        // Envoyer un email à l'administrateur nouvellement créé
        try {
            String emailMessage = sendEmailService.mailTemplateAdminCreated(
                existingUser.getFullName() != null ? existingUser.getFullName() : existingUser.getEmail(),
                existingUser.getEmail(),
                frontendUrl
            );
            sendEmailService.sendEmailWithAttachment(
                existingUser.getEmail(),
                emailMessage,
                "Votre compte administrateur a été créé - Orange Digital Center"
            );
        } catch (Exception e) {
            // Ne pas faire échouer la création si l'email échoue
            System.err.println("Erreur lors de l'envoi de l'email à l'administrateur: " + e.getMessage());
            e.printStackTrace();
        }

        // Retourner le DTO avec les données utilisateur
        AdminWithUserDto adminDto = AdminWithUserDto.fromAdmin(savedAdmin);
        
        return CResponse.success(adminDto, "Administrateur créé avec succès.");
    }

    public CResponse<?> getAllAdmins() {
        // Utiliser JOIN FETCH pour charger la relation user de manière eager
        List<Admin> admins = adminRepository.findAllWithUserJoinFetch();
        // Convertir en DTO pour inclure les données User dans la réponse JSON
        List<AdminWithUserDto> adminDtos = admins.stream()
                .map(AdminWithUserDto::fromAdmin)
                .collect(Collectors.toList());
        return CResponse.success(adminDtos, "Liste des administrateurs.");
    }

    public CResponse<?> getAdminById(Long id) {
        Optional<Admin> adminOptional = adminRepository.findByIdWithUser(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            AdminWithUserDto adminDto = AdminWithUserDto.fromAdmin(admin);
            return CResponse.success(adminDto, "Administrateur trouvé.");
        }
        return CResponse.error("Administrateur non trouvé avec l'ID: " + id);
    }

    @Transactional
    public CResponse<?> updateAdmin(Long id, User userDetails) {
        // Charger l'admin avec son user de manière eager
        Optional<Admin> adminOptional = adminRepository.findByIdWithUser(id);
        if (adminOptional.isEmpty()) {
            return CResponse.error("Administrateur non trouvé avec l'ID: " + id);
        }
        
        Admin admin = adminOptional.get();
        User existingUser = admin.getUser();
        
        // S'assurer que l'utilisateur est bien chargé
        if (existingUser == null) {
            return CResponse.error("Utilisateur associé non trouvé pour l'administrateur avec l'ID: " + id);
        }
        
        // Mettre à jour uniquement les champs non-null fournis dans userDetails
        // Préserver tous les autres champs existants
        if (userDetails.getFullName() != null && !userDetails.getFullName().trim().isEmpty()) {
            existingUser.setFullName(userDetails.getFullName().trim());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail().trim());
        }
        // Phone peut être une chaîne vide, donc on vérifie explicitement null
        if (userDetails.getPhone() != null) {
            existingUser.setPhone(userDetails.getPhone().trim().isEmpty() ? null : userDetails.getPhone().trim());
        }
        if (userDetails.getAvatar() != null) {
            existingUser.setAvatar(userDetails.getAvatar());
        }
        if (userDetails.getActivate() != null) {
            existingUser.setActivate(userDetails.getActivate());
        }
        // Ne pas mettre à jour le password si non fourni (pour éviter de l'écraser)
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            // Le password devrait être encodé, mais on ne le fait pas ici pour éviter les problèmes
            // Si nécessaire, utiliser PasswordEncoder dans le service
        }
        
        // Sauvegarder l'utilisateur mis à jour
        User savedUser = userRepository.save(existingUser);
        
        // Retourner l'admin avec les données utilisateur mises à jour
        Admin savedAdmin = adminRepository.save(admin);
        
        // Convertir en DTO pour la réponse
        AdminWithUserDto adminDto = AdminWithUserDto.fromAdmin(savedAdmin);
        
        return CResponse.success(adminDto, "Administrateur mis à jour avec succès.");
    }

    @Transactional
    public CResponse<?> deleteAdmin(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            User user = admin.getUser();
            
            // Supprimer l'utilisateur, ce qui supprimera automatiquement l'Admin en cascade
            // grâce à CascadeType.ALL et orphanRemoval = true dans la relation User -> Admin
            userRepository.delete(user);
            
            return CResponse.success(null, "Administrateur et utilisateur associé supprimés avec succès.");
        }
        return CResponse.error("Administrateur non trouvé avec l'ID: " + id);
    }
}
