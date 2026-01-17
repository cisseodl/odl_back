package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.CascadeType; // Added
import java.util.Objects; // Added for equals/hashCode
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added
import java.util.ArrayList; // Added for default list initialization
import java.util.List;
import javax.persistence.OneToMany;


// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "reponses")
// @Data // Removed
public class Reponses extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;
    @Column(nullable = true)
    private Boolean isCorrect; // Indique si cette réponse est correcte (pour les QUIZ)
    @ManyToOne
    @JsonBackReference // Added (corresponds to Questions.reponses @JsonManagedReference)
    private Questions questions;

    @OneToMany(mappedBy = "reponse", cascade = CascadeType.ALL, orphanRemoval = true) // Added for Answer
    @JsonManagedReference // Added for Answer
    private List<Answer> answers = new ArrayList<>();


    public Reponses() {
        super();
    }

    public Reponses(String title, String description, String status, String imagePath, Questions questions, List<Answer> answers) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.imagePath = imagePath;
        this.questions = questions;
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
    
    public Boolean getIsCorrect() {
        return isCorrect;
    }
    
    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
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
        Reponses reponses = (Reponses) o;
        return Objects.equals(title, reponses.title) &&
               Objects.equals(description, reponses.description) &&
               Objects.equals(status, reponses.status) &&
               Objects.equals(imagePath, reponses.imagePath) &&
               Objects.equals(questions, reponses.questions) &&
               Objects.equals(answers, reponses.answers); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, status, imagePath, questions, answers); // Added
    }

    @Override
    public String toString() {
        return "Reponses{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", status='" + status + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", questions=" + (questions != null ? questions.getId() : "null") +
               ", answers=" + (answers != null ? answers.size() : "null") + // Added
               ", id=" + id +
               '}';
    }
}
