package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.service.CohorteService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content; // Added
import io.swagger.v3.oas.annotations.media.ExampleObject; // Added
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Added

@RequestMapping("/cohorte")
@RestController

public class CohorteController {

    private final CohorteService cohorteService;

    public CohorteController(CohorteService cohorteService) {
        this.cohorteService = cohorteService;
    }

    @GetMapping("/read")
    // Permettre l'accès sans authentification pour permettre le chargement des cohortes lors de l'inscription
    public CResponse<?> getAllCohortes() {
        return cohorteService.getAllCohortes();
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getCohorteById(@PathVariable Long id) {
        return cohorteService.getCohorteById(id);

    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestBody(
        description = "Données pour la création d'une nouvelle Cohorte (les champs d'audit sont générés automatiquement)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Création de Cohorte",
                    value = "{\"nom\": \"Nouvelle Cohorte\", \"description\": \"Description détaillée de la cohorte.\", \"dateDebut\": \"2026-01-04T12:00:00\", \"dateFin\": \"2026-01-04T18:00:00\"}"
                )
            }
        )
    )
    public CResponse<?> createCohorte(@org.springframework.web.bind.annotation.RequestBody Cohorte cohorte) {
           return cohorteService.createCohorte(cohorte);

    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> updateCohorte(@org.springframework.web.bind.annotation.RequestBody Cohorte cohorte) {
       return cohorteService.updateCohorte(cohorte);


    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> deleteCohorte(@PathVariable Long id) {

        return cohorteService.deleteCohorte(id);

    }
}
