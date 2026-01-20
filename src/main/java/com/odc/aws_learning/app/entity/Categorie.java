package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // Ajouté

import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Lombok removed
// import lombok.EqualsAndHashCode; // Lombok removed

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.CascadeType; // Added
import java.util.Objects; // Added for equals/hashCode
import java.util.ArrayList; // Added for default list initialization
import java.util.List;
import javax.persistence.OneToMany; // Added
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added


// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "categorie")
// @Data // Lombok removed
public class Categorie extends BaseEntity {
    private String title;
    @Lob
    private String description;

    /**
     * Relation OneToMany vers Formation (nouvelle hiérarchie)
     */
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for Formation
    private List<Formation> formations = new ArrayList<>();

    /**
     * Ancienne relation vers Courses (dépréciée, conservée pour migration)
     * Les cours doivent maintenant être liés à une Formation
     */
    @Deprecated
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Courses> courses = new ArrayList<>();

    public Categorie() {
        super();
    }

    public Categorie(String title, String description, List<Courses> courses) {
        this.title = title;
        this.description = description;
        this.courses = courses;
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

    @JsonIgnore // Ajouté pour éviter les références circulaires lors de la sérialisation
    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations = formations;
    }

    @JsonIgnore
    @Deprecated
    public List<Courses> getCourses() {
        return courses;
    }

    @Deprecated
    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Categorie categorie = (Categorie) o;
        return Objects.equals(title, categorie.title) && 
               Objects.equals(description, categorie.description) && 
               Objects.equals(formations, categorie.formations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, formations);
    }

    @Override
    public String toString() {
        return "Categorie{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", formations=" + (formations != null ? formations.size() : "null") +
               ", courses=" + (courses != null ? courses.size() : "null") +
               ", id=" + id +
               '}';
    }
}
