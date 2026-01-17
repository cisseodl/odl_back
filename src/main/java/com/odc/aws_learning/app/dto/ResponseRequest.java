package com.odc.aws_learning.app.dto;

import lombok.Data;

@Data
public class ResponseRequest {
    private String title; // Texte de la réponse
    private String description; // Description optionnelle
    private Boolean isCorrect; // true si c'est la bonne réponse, false sinon
}
