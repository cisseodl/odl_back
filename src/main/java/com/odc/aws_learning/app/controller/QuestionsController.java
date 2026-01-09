package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.service.QuestionsService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content; // Added
import io.swagger.v3.oas.annotations.media.ExampleObject; // Added
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Added

@RequestMapping("/questions")
@RestController
public class QuestionsController {
    private final QuestionsService questionsService;
    public QuestionsController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestBody(
        description = "Données pour la création d'une nouvelle Question (les champs d'audit sont générés automatiquement)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Création de Question",
                    value = "{\"title\": \"Titre de la Question\", \"description\": \"Description de la question.\", \"status\": \"ACTIVE\", \"imagePath\": \"url_image.png\", \"type\": \"QCM\"}"
                )
            }
        )
    )
    public CResponse<?> saveQuestions(@org.springframework.web.bind.annotation.RequestBody Questions questions) {
        return questionsService.saveQuestions(questions);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getAll() {
        return questionsService.getAll();
    }
}
