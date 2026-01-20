package com.odc.aws_learning.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Getter
@Setter
public class SatisfactionRequest {
    @NotBlank(message = "La satisfaction est obligatoire")
    private String satisfaction;

    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    private Integer rating; // Optionnel, de 1 à 5
}
