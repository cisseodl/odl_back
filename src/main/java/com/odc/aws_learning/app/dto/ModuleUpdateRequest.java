package com.odc.aws_learning.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ModuleUpdateRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Integer moduleOrder;
    private List<LessonUpdateRequest> lessons;

    public ModuleUpdateRequest() {
    }

    public ModuleUpdateRequest(Long id, String title, String description, Integer moduleOrder, List<LessonUpdateRequest> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.moduleOrder = moduleOrder;
        this.lessons = lessons;
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

    public List<LessonUpdateRequest> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonUpdateRequest> lessons) {
        this.lessons = lessons;
    }
}
