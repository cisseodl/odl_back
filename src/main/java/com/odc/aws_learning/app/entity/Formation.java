package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité Formation : niveau intermédiaire entre Catégorie et Cours
 * Hiérarchie : Catégorie -> Formation -> Cours -> Module -> Leçon
 */
@Entity
@Table(name = "formation")
public class Formation extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    /**
     * Relation ManyToOne vers Catégorie
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonBackReference
    private Categorie categorie;

    /**
     * Relation OneToMany vers Cours
     */
    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Courses> courses = new ArrayList<>();

    public Formation() {
        super();
    }

    public Formation(String title, String description, String imagePath, Categorie categorie, List<Courses> courses) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.categorie = categorie;
        this.courses = courses != null ? courses : new ArrayList<>();
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Formation formation = (Formation) o;
        return Objects.equals(title, formation.title) &&
               Objects.equals(description, formation.description) &&
               Objects.equals(imagePath, formation.imagePath) &&
               Objects.equals(categorie, formation.categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, imagePath, categorie);
    }

    @Override
    public String toString() {
        return "Formation{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", categorie=" + (categorie != null ? categorie.getId() : "null") +
               ", courses=" + (courses != null ? courses.size() : "null") +
               ", id=" + id +
               '}';
    }
}

