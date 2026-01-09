package com.odc.aws_learning.app.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Replaced
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType; // Added
import java.util.List;
import java.util.Objects; // Added for equals/hashCode
import java.util.ArrayList; // Added for default list initialization

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "evaluations")
// @Data // Removed

public class Evaluations extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;

    @OneToMany(mappedBy = "evaluations")
    // @JsonIgnoreProperties(value = {"evaluations"}, allowSetters = true) // Replaced by @JsonManagedReference
    @JsonManagedReference // Added
    private List<Questions> questions = new ArrayList<>(); // Initialize to avoid NullPointerException

    @OneToMany(mappedBy = "evaluations", cascade = CascadeType.ALL, orphanRemoval = true) // Added for InfoTest
    @JsonManagedReference // Added for InfoTest
    private List<InfoTest> infoTests = new ArrayList<>();

    public Evaluations() {
        super();
    }

    public Evaluations(String title, String description, String status, String imagePath, List<Questions> questions, List<InfoTest> infoTests) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.imagePath = imagePath;
        this.questions = questions;
        this.infoTests = infoTests;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    public List<InfoTest> getInfoTests() {
        return infoTests;
    }

    public void setInfoTests(List<InfoTest> infoTests) {
        this.infoTests = infoTests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Evaluations that = (Evaluations) o;
        return Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(status, that.status) &&
               Objects.equals(imagePath, that.imagePath) &&
               Objects.equals(questions, that.questions) &&
               Objects.equals(infoTests, that.infoTests); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, status, imagePath, questions, infoTests); // Added
    }

    @Override
    public String toString() {
        return "Evaluations{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", status='" + status + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", questions=" + (questions != null ? questions.size() : "null") + // Avoid recursion
               ", infoTests=" + (infoTests != null ? infoTests.size() : "null") + // Added
               ", id=" + id +
               '}';
    }
}
