package com.odc.aws_learning.app.constante;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CourseLevel {
    DEBUTANT,
    INTERMEDIAIRE,
    AVANCE;

    @JsonCreator
    public static CourseLevel fromString(String value) {
        String upperValue = value.toUpperCase();
        switch (upperValue) {
            case "BEGINNER":
            case "DEBUTANT":
                return DEBUTANT;
            case "INTERMEDIATE":
            case "INTERMEDIAIRE":
                return INTERMEDIAIRE;
            case "ADVANCED":
            case "AVANCE":
                return AVANCE;
            default:
                throw new IllegalArgumentException("Niveau invalide: " + value);
        }
    }
}
