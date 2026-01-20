package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.CourseLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public class CourseUpdateRequest {
    @NotBlank
    private String title;
    private String subtitle;
    @NotBlank
    private String description;
    private String imagePath;
    @NotNull
    private Long instructorId;
    /**
     * ID de la formation (nouvelle hiérarchie)
     * Si formationId est fourni, il sera utilisé. Sinon, categoryId sera utilisé pour la compatibilité.
     */
    private Long formationId;
    /**
     * ID de la catégorie (ancien système, déprécié)
     * Utilisé uniquement si formationId n'est pas fourni
     */
    private Long categoryId;
    private CourseLevel level;
    private String language;

    private Set<String> objectives;
    private Set<String> features;
    private Boolean bestseller;
    private List<ModuleUpdateRequest> modules;
    private com.odc.aws_learning.app.constante.CourseStatus status;

    public CourseUpdateRequest() {
    }

    public CourseUpdateRequest(String title, String subtitle, String description, String imagePath, Long instructorId, Long categoryId, CourseLevel level, String language, Set<String> objectives, Set<String> features, Boolean bestseller, List<ModuleUpdateRequest> modules, com.odc.aws_learning.app.constante.CourseStatus status) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imagePath = imagePath;
        this.instructorId = instructorId;
        this.categoryId = categoryId;
        this.level = level;
        this.language = language;
        this.objectives = objectives;
        this.features = features;
        this.bestseller = bestseller;
        this.modules = modules;
        this.status = status;
    }
    // ... (getters/setters existants)

    public com.odc.aws_learning.app.constante.CourseStatus getStatus() {
        return status;
    }

    public void setStatus(com.odc.aws_learning.app.constante.CourseStatus status) {
        this.status = status;
    }

    public List<ModuleUpdateRequest> getModules() {
        return modules;
    }

    public void setModules(List<ModuleUpdateRequest> modules) {
        this.modules = modules;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(Set<String> objectives) {
        this.objectives = objectives;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public Boolean getBestseller() {
        return bestseller;
    }

    public void setBestseller(Boolean bestseller) {
        this.bestseller = bestseller;
    }
}
