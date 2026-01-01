package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.LessonType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LessonUpdateRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    private Integer lessonOrder; // Renommé pour éviter le mot-clé SQL
    @NotNull
    private LessonType type;
    private String contentUrl;
    private Integer duration; // en minutes

    public LessonUpdateRequest() {
    }

    public LessonUpdateRequest(Long id, String title, Integer lessonOrder, LessonType type, String contentUrl, Integer duration) {
        this.id = id;
        this.title = title;
        this.lessonOrder = lessonOrder;
        this.type = type;
        this.contentUrl = contentUrl;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(Integer lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
