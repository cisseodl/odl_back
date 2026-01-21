package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.auth.dao.request.ApprenantCreateRequest;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.service.ApprenantService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User; // Keep for other methods if needed
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // Keep for other methods
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag; // Ajout de l'import Tag

import java.security.Principal; // Added for Principal

@Tag(name = "Apprenant Management", description = "Endpoints for creating and managing apprenants") // Ajout de l'annotation Swagger Tag
@RequestMapping("/api/apprenants")
@RestController
@RequiredArgsConstructor
public class ApprenantController {
    private final ApprenantService apprenantService;

    // Endpoint for creating an Apprenant (Authenticated User creates their own Apprenant profile)
    @Operation(summary = "Crée un nouveau profil Apprenant pour l'utilisateur authentifié")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()") // Only authenticated users can create their Apprenant profile
    @RequestBody(
        description = "Création d'un profil Apprenant pour l'utilisateur authentifié",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Create Apprenant Example (Authenticated User)",
                    value = "{\"activate\": true, \"username\": \"Jean Dupont\", \"numero\": \"0123456789\", \"profession\": \"Etudiant\", \"niveauEtude\": \"Master\", \"filiere\": \"Informatique\", \"attentes\": \"Apprendre le développement backend avec Spring Boot\", \"conditionsAccepted\": true, \"cohorteId\": 1}"
                )
            }
        )
    )
    public CResponse<?> create(
            Principal principal, // To get authenticated user's details
            @org.springframework.web.bind.annotation.RequestBody ApprenantCreateRequest request) {
        // Si userId ou userEmail est fourni dans la requête, l'utiliser (pour permettre aux admins de créer un apprenant pour un autre utilisateur)
        // Sinon, utiliser l'email de l'utilisateur connecté (comportement par défaut)
        String emailToUse = request.getUserEmail() != null ? request.getUserEmail() : principal.getName();
        return apprenantService.createApprenantAuthenticated(
            emailToUse, // User's email from request or JWT
            request
        );
    }

    /**
     * Récupérer tous les apprenants
     */
    @GetMapping(
            value = "/get-all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'APPRENANT')")
    public CResponse<?> getAll() {
        return apprenantService.getAllApprenants();
    }

    /**
     * Récupérer un apprenant par ID
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'APPRENANT')")
    public CResponse<?> getApprenantById(@PathVariable Long id) {
        return apprenantService.getApprenantById(id);
    }

    /**
     * Récupérer les statistiques d'un apprenant (cours inscrits, complétés, certificats)
     */
    @GetMapping(
            value = "/{id}/stats",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'APPRENANT')")
    public CResponse<?> getApprenantStats(@PathVariable Long id) {
        return apprenantService.getApprenantStats(id);
    }

    /**
     * Mise à jour d'un apprenant
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'APPRENANT')")
    public CResponse<?> updateApprenant(
            @PathVariable Long id,
            @RequestBody User userDetails,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String niveauEtude,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String attentes,
            @RequestParam(required = false) Boolean conditionsAccepted,
            @RequestParam(required = false) @Deprecated Boolean satisfaction, // Ancien nom, déprécié
            @RequestParam(required = false) Long cohorteId
    ) {
        return apprenantService.updateApprenant(
                id,
                userDetails,
                username,
                numero,
                profession,
                niveauEtude,
                filiere,
                cohorteId,
                attentes,
                conditionsAccepted != null ? conditionsAccepted : satisfaction // Utiliser conditionsAccepted si fourni, sinon satisfaction (compatibilité)
        );
    }

    /**
     * Suppression d'un apprenant
     */
    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> deleteApprenant(@PathVariable Long id) {
        return apprenantService.deleteApprenant(id);
    }

    /**
     * Liste des apprenants par cohorte (pagination)
     */
    @GetMapping(
            value = "/get-by-cohorte/{cohorteId}/{page}/{size}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'APPRENANT')")
    public CResponse<?> getByCohorte(
            @PathVariable Long cohorteId,
            @PathVariable int page,
            @PathVariable int size
    ) {
        return apprenantService.getByCohorte(cohorteId, page, size);
    }
}
