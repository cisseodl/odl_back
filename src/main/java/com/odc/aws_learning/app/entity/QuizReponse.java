package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Removed
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.*;
import java.util.Objects; // Added for equals/hashCode

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity
@Table(name = "quiz_reponse")
// @Data // Removed
public class QuizReponse extends BaseEntity {
    
    @Lob
    private String texte;
    
    private Boolean estCorrecte; // true si c'est la bonne réponse
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    // @JsonIgnoreProperties(value = {"reponses"}, allowSetters = true) // Replaced by @JsonBackReference
    @JsonBackReference // Added (corresponds to QuizQuestion.reponses @JsonManagedReference)
    private QuizQuestion question;

    public QuizReponse() {
        super();
    }

    public QuizReponse(String texte, Boolean estCorrecte, QuizQuestion question) {
        this.texte = texte;
        this.estCorrecte = estCorrecte;
        this.question = question;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Boolean getEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(Boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuizReponse that = (QuizReponse) o;
        return Objects.equals(texte, that.texte) &&
               Objects.equals(estCorrecte, that.estCorrecte) &&
               Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), texte, estCorrecte, question);
    }

    @Override
    public String toString() {
        return "QuizReponse{" +
               "texte='" + texte + '\'' +
               ", estCorrecte=" + estCorrecte +
               ", question=" + (question != null ? question.getId() : "null") +
               ", id=" + id +
               '}';
    }
}
