package com.odc.aws_learning.app.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReviewRequest {
    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note doit être au moins 1")
    @Max(value = 5, message = "La note doit être au plus 5")
    private Integer rating;

    @NotBlank(message = "Le commentaire est obligatoire")
    private String comment;

    public ReviewRequest() {
    }

    public ReviewRequest(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
