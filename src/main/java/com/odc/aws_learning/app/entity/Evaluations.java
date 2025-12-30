package com.odc.aws_learning.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "evaluations")
@Data

public class Evaluations extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;

    @OneToMany(mappedBy = "evaluations")
    @JsonIgnoreProperties(value = {"evaluations"}, allowSetters = true)
    private List<Questions> questions;

}
