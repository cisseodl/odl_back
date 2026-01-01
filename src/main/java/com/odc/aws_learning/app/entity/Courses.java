package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Courses extends BaseEntity {

    private String title;

    private String subtitle;

    @Lob
    private String description;

    private String imagePath;

    private Integer duration; // en secondes

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    private String language;

    private Boolean bestseller = false;


    @ManyToOne(fetch = FetchType.LAZY)
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    private User instructor;

    @ElementCollection
    @CollectionTable(name = "course_objectives", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "objective")
    private Set<String> objectives = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "course_features", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "feature")
    private Set<String> features = new HashSet<>();

    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Module> modules = new ArrayList<>();

    // NoArgsConstructor
    public Courses() {
    }

    // AllArgsConstructor
    public Courses(String title, String subtitle, String description, String imagePath, Integer duration, CourseLevel level, String language, Boolean bestseller, Categorie categorie, User instructor, Set<String> objectives, Set<String> features, List<Module> modules) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imagePath = imagePath;
        this.duration = duration;
        this.level = level;
        this.language = language;
        this.bestseller = bestseller;
        this.categorie = categorie;
        this.instructor = instructor;
        this.objectives = objectives;
        this.features = features;
        this.modules = modules;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Integer getDuration() {
        return duration;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public String getLanguage() {
        return language;
    }

    public Boolean getBestseller() {
        return bestseller;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public User getInstructor() {
        return instructor;
    }

    public Set<String> getObjectives() {
        return objectives;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public List<Module> getModules() {
        return modules;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBestseller(Boolean bestseller) {
        this.bestseller = bestseller;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public void setObjectives(Set<String> objectives) {
        this.objectives = objectives;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Courses courses = (Courses) o;
        return Objects.equals(title, courses.title) &&
               Objects.equals(subtitle, courses.subtitle) &&
               Objects.equals(description, courses.description) &&
               Objects.equals(imagePath, courses.imagePath) &&
               Objects.equals(duration, courses.duration) &&
               level == courses.level &&
               Objects.equals(language, courses.language) &&
               Objects.equals(bestseller, courses.bestseller) &&
               Objects.equals(categorie, courses.categorie) &&
               Objects.equals(instructor, courses.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, subtitle, description, imagePath, duration, level, language, bestseller, categorie, instructor);
    }
}
