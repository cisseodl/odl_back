package com.odc.aws_learning.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ModuleCreationRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Integer moduleOrder;
    private List<LessonCreationRequest> lessons;

    public ModuleCreationRequest() {
    }

    public ModuleCreationRequest(String title, String description, Integer moduleOrder, List<LessonCreationRequest> lessons) {
        this.title = title;
        this.description = description;
        this.moduleOrder = moduleOrder;
        this.lessons = lessons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getModuleOrder() {
        return moduleOrder;
    }

    public void setModuleOrder(Integer moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public List<LessonCreationRequest> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonCreationRequest> lessons) {
        this.lessons = lessons;
    }
}
