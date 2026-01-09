package com.odc.aws_learning.app.controller;


import com.odc.aws_learning.app.service.EvaluationsService;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content; // Added
import io.swagger.v3.oas.annotations.media.ExampleObject; // Added
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Added

@RequestMapping("/evaluations")
@RestController

public class EvaluationsController {
    private final EvaluationsService evaluationsService;
    public EvaluationsController(EvaluationsService evaluationsService) {
        this.evaluationsService = evaluationsService;
    }
    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()") // Added for security
    @RequestBody(
        description = "Données pour la création d'une nouvelle Évaluation",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Création d'Évaluation",
                    value = "{\n" +
                            "  \"evaluationTitle\": \"Titre de l'évaluation\",\n" +
                            "  \"questionsList\": [\n" +
                            "    {\n" +
                            "      \"title\": \"Question 1\",\n" +
                            "      \"description\": \"Description de la question 1\",\n" +
                            "      \"status\": \"ACTIVE\",\n" +
                            "      \"imagePath\": \"url_image_question1.png\",\n" +
                            "      \"type\": \"QCM\",\n" +
                            "      \"reponses\": [\n" +
                            "        {\n" +
                            "          \"title\": \"Réponse A\",\n" +
                            "          \"description\": \"Description réponse A\",\n" +
                            "          \"status\": \"ACTIVE\",\n" +
                            "          \"imagePath\": \"url_image_reponseA.png\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                )
            }
        )
    )
    public CResponse<?> saveEvaluations(@org.springframework.web.bind.annotation.RequestBody Quiz_Answer quiz_answer) {
        return evaluationsService.createEvaluation(quiz_answer);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getAll() {
        return evaluationsService.getAll();
    }
}
