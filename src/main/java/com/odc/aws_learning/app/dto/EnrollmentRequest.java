package com.odc.aws_learning.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EnrollmentRequest {
    @NotBlank(message = "Les attentes sont obligatoires")
    private String expectations;
}
