package com.odc.aws_learning.app.entity;


import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType; // Added
import java.util.Objects; // Added for equals/hashCode
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added
import java.util.ArrayList; // Added for default list initialization
import java.util.List;
import javax.persistence.OneToMany;

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "infotest")
// @Data // Removed

public class InfoTest extends BaseEntity {
    @ManyToOne
    @JsonBackReference // Added (assuming Evaluations will have a List<InfoTest>)
    Evaluations evaluations;

    @OneToMany(mappedBy = "infoTest", cascade = CascadeType.ALL, orphanRemoval = true) // Added for Answer
    @JsonManagedReference // Added for Answer
    private List<Answer> answers = new ArrayList<>();

    public InfoTest() {
        super();
    }

    public InfoTest(Evaluations evaluations, List<Answer> answers) {
        this.evaluations = evaluations;
        this.answers = answers;
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
        InfoTest infoTest = (InfoTest) o;
        return Objects.equals(evaluations, infoTest.evaluations) &&
               Objects.equals(answers, infoTest.answers); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), evaluations, answers); // Added
    }

    @Override
    public String toString() {
        return "InfoTest{" +
               "evaluations=" + (evaluations != null ? evaluations.getId() : "null") +
               ", answers=" + (answers != null ? answers.size() : "null") + // Added
               ", id=" + id +
               '}';
    }
}
