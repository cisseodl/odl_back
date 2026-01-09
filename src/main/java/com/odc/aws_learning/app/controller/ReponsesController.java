package com.odc.aws_learning.app.controller;


import com.odc.aws_learning.app.entity.Reponses;
import com.odc.aws_learning.app.service.ReponsesService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content; // Added
import io.swagger.v3.oas.annotations.media.ExampleObject; // Added
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Added


@RequestMapping("/reponses")
@RestController

public class ReponsesController {
    private final ReponsesService reponsesService;
    public ReponsesController(ReponsesService reponsesService) {
        this.reponsesService = reponsesService;

    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestBody(
        description = "Données pour la création d'une nouvelle Réponse (les champs d'audit sont générés automatiquement)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Création de Réponse",
                    value = "{\"title\": \"Titre de la Réponse\", \"description\": \"Description de la réponse.\", \"status\": \"CORRECT\", \"imagePath\": \"url_image_reponse.png\"}"
                )
            }
        )
    )
    public CResponse<?> saveReponses(@org.springframework.web.bind.annotation.RequestBody Reponses reponses) {
        return reponsesService.saveReponses(reponses);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getAll() {
        return reponsesService.getAll();
    }
}


