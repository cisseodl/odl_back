package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "questions")
@Data
public class Questions extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;
    private String type;

    @OneToMany(mappedBy = "questions")
    @JsonIgnoreProperties(value = {"questions"}, allowSetters = true)
    private List<Reponses> reponses;

    @ManyToOne
    private  Evaluations evaluations;

}

