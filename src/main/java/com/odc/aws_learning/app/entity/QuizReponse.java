package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "quiz_reponse")
@Data
public class QuizReponse extends BaseEntity {
    
    @Lob
    private String texte;
    
    private Boolean estCorrecte; // true si c'est la bonne réponse
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnoreProperties(value = {"reponses"}, allowSetters = true)
    private QuizQuestion question;
}
