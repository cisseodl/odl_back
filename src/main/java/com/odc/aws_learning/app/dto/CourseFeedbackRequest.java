package com.odc.aws_learning.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CourseFeedbackRequest {
    @NotBlank(message = "Votre avis sur le cours est obligatoire")
    private String satisfaction;
}
