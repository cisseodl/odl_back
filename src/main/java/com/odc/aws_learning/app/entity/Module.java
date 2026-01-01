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

@Entity
@Table(name = "modules")
public class Module extends BaseEntity {

    private String title;
    private String description; // <-- Ajouté
    private Integer moduleOrder; // Pour éviter le mot-clé "order"

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Courses course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    // NoArgsConstructor
    public Module() {
    }

    // AllArgsConstructor
    public Module(String title, String description, Integer moduleOrder, Courses course, List<Lesson> lessons) { // <-- description ajouté
        this.title = title;
        this.description = description; // <-- Ajouté
        this.moduleOrder = moduleOrder;
        this.course = course;
        this.lessons = lessons;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() { // <-- Ajouté
        return description;
    }

    public Integer getModuleOrder() {
        return moduleOrder;
    }

    public Courses getCourse() {
        return course;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) { // <-- Ajouté
        this.description = description;
    }

    public void setModuleOrder(Integer moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Module module = (Module) o;
        return Objects.equals(title, module.title) &&
               Objects.equals(description, module.description) && // <-- Ajouté
               Objects.equals(moduleOrder, module.moduleOrder) &&
               Objects.equals(course, module.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, moduleOrder, course); // <-- description ajouté
    }
}
