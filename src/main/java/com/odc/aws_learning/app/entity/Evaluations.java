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
    public enum EvaluationType {
        QUIZ,  // Évaluation avec questions/réponses automatiques
        TP     // Travaux pratiques corrigés par l'instructeur
    }
    
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationType type = EvaluationType.QUIZ; // Par défaut QUIZ
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;
    
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor; // Instructeur qui crée l'évaluation
    
    @Column(nullable = true, length = 5000)
    private String tpInstructions; // Instructions pour les TPs (optionnel)
    
    @Column(nullable = true, length = 1000)
    private String tpFileUrl; // Fichier TP à télécharger (optionnel)

    @OneToMany(mappedBy = "evaluations")
    // @JsonIgnoreProperties(value = {"evaluations"}, allowSetters = true) // Replaced by @JsonManagedReference
    @JsonManagedReference // Added
    private List<Questions> questions = new ArrayList<>(); // Initialize to avoid NullPointerException

    @OneToMany(mappedBy = "evaluations", cascade = CascadeType.ALL, orphanRemoval = true) // Added for InfoTest
    @JsonManagedReference // Added for InfoTest
    private List<InfoTest> infoTests = new ArrayList<>();
    
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<com.odc.aws_learning.app.entity.EvaluationAttempt> attempts = new ArrayList<>();

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
    
    public EvaluationType getType() {
        return type;
    }
    
    public void setType(EvaluationType type) {
        this.type = type;
    }
    
    public Courses getCourse() {
        return course;
    }
    
    public void setCourse(Courses course) {
        this.course = course;
    }
    
    public User getInstructor() {
        return instructor;
    }
    
    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }
    
    public String getTpInstructions() {
        return tpInstructions;
    }
    
    public void setTpInstructions(String tpInstructions) {
        this.tpInstructions = tpInstructions;
    }
    
    public String getTpFileUrl() {
        return tpFileUrl;
    }
    
    public void setTpFileUrl(String tpFileUrl) {
        this.tpFileUrl = tpFileUrl;
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
    
    public List<com.odc.aws_learning.app.entity.EvaluationAttempt> getAttempts() {
        return attempts;
    }
    
    public void setAttempts(List<com.odc.aws_learning.app.entity.EvaluationAttempt> attempts) {
        this.attempts = attempts;
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
               Objects.equals(infoTests, that.infoTests) &&
               type == that.type &&
               Objects.equals(course, that.course) &&
               Objects.equals(instructor, that.instructor);
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
               ", type=" + type +
               ", course=" + (course != null ? course.getId() : "null") +
               ", instructor=" + (instructor != null ? instructor.getId() : "null") +
               ", id=" + id +
               '}';
    }
}
