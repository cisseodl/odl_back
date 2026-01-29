package com.odc.aws_learning.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseValidationRequest {

    @NotNull
    private ValidationAction action;

    private String reason;

    public enum ValidationAction {
        APPROVE,
        REJECT,
        /** Rétirer un cours publié (PUBLIE -> BROUILLON), sans suppression. */
        WITHDRAW
    }
}
