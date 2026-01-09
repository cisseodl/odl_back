package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects; // Added for equals/hashCode
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "answers")
// @Data // Removed
public class Answer extends BaseEntity {
    @ManyToOne
    @JsonBackReference // Added
    private Questions question;

    @ManyToOne
    @JsonBackReference // Added
    private Reponses reponse;

    @ManyToOne
    @JsonBackReference // Added
    private InfoTest infoTest;

    public Answer() {
        super();
    }

    public Answer(Questions question, Reponses reponse, InfoTest infoTest) {
        this.question = question;
        this.reponse = reponse;
        this.infoTest = infoTest;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public Reponses getReponse() {
        return reponse;
    }

    public void setReponse(Reponses reponse) {
        this.reponse = reponse;
    }

    public InfoTest getInfoTest() {
        return infoTest;
    }

    public void setInfoTest(InfoTest infoTest) {
        this.infoTest = infoTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(question, answer.question) &&
               Objects.equals(reponse, answer.reponse) &&
               Objects.equals(infoTest, answer.infoTest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), question, reponse, infoTest);
    }

    @Override
    public String toString() {
        return "Answer{" +
               "question=" + (question != null ? question.getId() : "null") +
               ", reponse=" + (reponse != null ? reponse.getId() : "null") +
               ", infoTest=" + (infoTest != null ? infoTest.getId() : "null") +
               ", id=" + id +
               '}';
    }
}
