package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.COURSE_TYPE;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "courses")
@Data

public class Courses extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String imagePath;
    private Integer duration;
    private COURSE_TYPE courseType;
    @ManyToOne
    private Categorie categorie;
}
