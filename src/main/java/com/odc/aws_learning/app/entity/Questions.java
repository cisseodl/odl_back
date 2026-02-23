package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Removed
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.util.List;
import java.util.Objects; // Added for equals/hashCode
import java.util.ArrayList; // Added for default list initialization

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "questions")
// @Data // Removed
public class Questions extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;
    private String type;
    /** Points attribués à cette question (pour le calcul du score pondéré). Si null ou 0, compte comme 1 point. */
    private Integer points;

    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL, orphanRemoval = true) // Add cascade/orphanRemoval as it's a @OneToMany
    // @JsonIgnoreProperties(value = {"questions"}, allowSetters = true) // Replaced by @JsonManagedReference
    @JsonManagedReference // Added
    private List<Reponses> reponses = new ArrayList<>(); // Initialize to avoid NullPointerException

    @ManyToOne
    @JsonBackReference // Added (corresponds to Evaluations.questions @JsonManagedReference)
    private  Evaluations evaluations;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true) // Added for Answer
    @JsonManagedReference // Added for Answer
    private List<Answer> answers = new ArrayList<>();


    public Questions() {
        super();
    }

    public Questions(String title, String description, String status, String imagePath, String type, List<Reponses> reponses, Evaluations evaluations, List<Answer> answers) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.imagePath = imagePath;
        this.type = type;
        this.reponses = reponses;
        this.evaluations = evaluations;
        this.answers = answers;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<Reponses> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponses> reponses) {
        this.reponses = reponses;
    }

    public Evaluations getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Evaluations evaluations) {
        this.evaluations = evaluations;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Questions questions = (Questions) o;
        return Objects.equals(title, questions.title) &&
               Objects.equals(description, questions.description) &&
               Objects.equals(status, questions.status) &&
               Objects.equals(imagePath, questions.imagePath) &&
               Objects.equals(type, questions.type) &&
               Objects.equals(points, questions.points) &&
               Objects.equals(reponses, questions.reponses) &&
               Objects.equals(evaluations, questions.evaluations) &&
               Objects.equals(answers, questions.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, status, imagePath, type, points, reponses, evaluations, answers);
    }

    @Override
    public String toString() {
        return "Questions{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", status='" + status + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", type='" + type + '\'' +
               ", points=" + points +
               ", reponses=" + (reponses != null ? reponses.size() : "null") +
               ", evaluations=" + (evaluations != null ? evaluations.getId() : "null") +
               ", answers=" + (answers != null ? answers.size() : "null") + // Added
               ", id=" + id +
               '}';
    }
}

