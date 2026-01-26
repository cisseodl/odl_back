package com.odc.aws_learning.app.dto;

import javax.validation.constraints.NotBlank;

public class TestimonialRequest {
    @NotBlank(message = "Le contenu du témoignage ne peut pas être vide.")
    private String content;

    public TestimonialRequest() {
    }

    public TestimonialRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
