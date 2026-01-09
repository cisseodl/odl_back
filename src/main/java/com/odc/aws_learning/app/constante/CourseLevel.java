package com.odc.aws_learning.app.constante;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CourseLevel {
    DEBUTANT,
    INTERMEDIAIRE,
    AVANCE;

    @JsonCreator
    public static CourseLevel fromString(String value) {
        return switch (value.toUpperCase()) {
            case "BEGINNER", "DEBUTANT" -> DEBUTANT;
            case "INTERMEDIATE", "INTERMEDIAIRE" -> INTERMEDIAIRE;
            case "ADVANCED", "AVANCE" -> AVANCE;
            default -> throw new IllegalArgumentException("Niveau invalide: " + value);
        };
    }
}
