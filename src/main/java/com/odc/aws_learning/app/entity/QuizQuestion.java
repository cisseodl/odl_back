package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "quiz_question")
@Data
public class QuizQuestion extends BaseEntity {
    
    @Lob
    private String contenu;
    
    @Enumerated(EnumType.STRING)
    private QuestionType type; // QCM ou TEXTE
    
    private Integer points; // Points attribués pour cette question
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnoreProperties(value = {"questions"}, allowSetters = true)
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"question"}, allowSetters = true)
    private List<QuizReponse> reponses;
    
    public enum QuestionType {
        QCM,    // Question à choix multiples
        TEXTE   // Question à réponse libre
    }
}
