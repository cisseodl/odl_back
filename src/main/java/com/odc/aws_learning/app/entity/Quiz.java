package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "quiz")
@Data
public class Quiz extends BaseEntity {
    
    private String titre;
    
    @Lob
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties(value = {"quiz"}, allowSetters = true)
    private Courses course;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"quiz"}, allowSetters = true)
    private List<QuizQuestion> questions;
    
    private Integer dureeMinutes; // Durée du quiz en minutes
    private Integer scoreMinimum; // Score minimum pour réussir
}
