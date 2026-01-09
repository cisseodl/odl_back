package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added


@Entity
@Table(name = "modules")
public class Module extends BaseEntity {

    private String title;
    private String description;
    private Integer moduleOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference // Added (corresponds to Courses.modules @JsonManagedReference)
    private Courses course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added (corresponds to Lesson.module @JsonBackReference)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true) // Added for LearnerModule
    @JsonManagedReference // Added for LearnerModule
    private List<LearnerModule> learnerModules = new ArrayList<>();


    public Module() {
    }

    public Module(String title, String description, Integer moduleOrder, Courses course, List<Lesson> lessons, List<LearnerModule> learnerModules) {
        this.title = title;
        this.description = description;
        this.moduleOrder = moduleOrder;
        this.course = course;
        this.lessons = lessons;
        this.learnerModules = learnerModules;
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

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<LearnerModule> getLearnerModules() {
        return learnerModules;
    }

    public void setLearnerModules(List<LearnerModule> learnerModules) {
        this.learnerModules = learnerModules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Module module = (Module) o;
        return Objects.equals(title, module.title) &&
               Objects.equals(description, module.description) &&
               Objects.equals(moduleOrder, module.moduleOrder) &&
               Objects.equals(course, module.course) &&
               Objects.equals(lessons, module.lessons) && // Added
               Objects.equals(learnerModules, module.learnerModules); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, moduleOrder, course, lessons, learnerModules); // Added
    }

    @Override
    public String toString() {
        return "Module{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", moduleOrder=" + moduleOrder +
               ", course=" + (course != null ? course.getId() : "null") +
               ", lessons=" + (lessons != null ? lessons.size() : "null") +
               ", learnerModules=" + (learnerModules != null ? learnerModules.size() : "null") +
               ", id=" + id +
               '}';
    }
}
